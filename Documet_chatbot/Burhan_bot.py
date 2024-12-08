import streamlit as st
import os
from PyPDF2 import PdfReader
import docx
from langchain.chat_models import ChatOpenAI
from dotenv import load_dotenv
from langchain.embeddings import HuggingFaceEmbeddings
from langchain.text_splitter import CharacterTextSplitter
from langchain.vectorstores import FAISS
from langchain.chains import ConversationalRetrievalChain
from langchain.memory import ConversationBufferMemory
from streamlit_chat import message
from langchain.callbacks import get_openai_callback
from sentence_transformers import SentenceTransformer

# Load API keys securely from .env file
load_dotenv()
openai_api_key = os.getenv("OPENAI_API_KEY")
huggingface_api_key = os.getenv("HUGGINGFACE_API_KEY")

def main():
    st.set_page_config(page_title="Burhan_DocumentGPT")
    st.header("Burhan_DocumentGPT")

    if "conversation" not in st.session_state:
        st.session_state.conversation = None
    if "chat_history" not in st.session_state:
        st.session_state.chat_history = None
    if "processComplete" not in st.session_state:
        st.session_state.processComplete = None

    # Sidebar for file upload and API key input
    with st.sidebar:
        uploaded_files = st.file_uploader("Upload your files", type=['pdf', 'docx'], accept_multiple_files=True)
        # Removed this line of code if you access the API from secrets.toml 
        user_openai_api_key = st.text_input("OpenAI API Key", type="password", value=openai_api_key)
        user_huggingface_api_key = st.text_input("Hugging Face API Key", type="password", value=huggingface_api_key)
        process = st.button("Process")

    if process:
        if not user_openai_api_key or not user_huggingface_api_key:
            st.info("Please enter both OpenAI and Hugging Face API keys to continue.")
            st.stop()

        files_text = get_files_text(uploaded_files)
        st.write("Files loaded...")

        # Split text into chunks
        text_chunks = get_text_chunks(files_text)
        st.write("Text chunks created...")

        # Create vector store
        vector_store = get_vectorstore(text_chunks, user_huggingface_api_key)
        st.write("Vector store created...")

        # Initialize conversation chain
        st.session_state.conversation = get_conversation_chain(vector_store, user_openai_api_key)
        st.session_state.processComplete = True

    if st.session_state.processComplete:
        user_question = st.chat_input("Ask a question about your files:")
        if user_question:
            handle_user_input(user_question)

# Function to extract text from uploaded files
def get_files_text(uploaded_files):
    text = ""
    for uploaded_file in uploaded_files:
        _, file_extension = os.path.splitext(uploaded_file.name)
        if file_extension == ".pdf":
            text += get_pdf_text(uploaded_file)
        elif file_extension == ".docx":
            text += get_docx_text(uploaded_file)
    return text

def get_pdf_text(pdf_file):
    pdf_reader = PdfReader(pdf_file)
    text = "".join(page.extract_text() for page in pdf_reader.pages)
    return text

def get_docx_text(docx_file):
    doc = docx.Document(docx_file)
    text = " ".join(paragraph.text for paragraph in doc.paragraphs)
    return text

# Function to split text into chunks
def get_text_chunks(text):
    text_splitter = CharacterTextSplitter(
        separator="\n",
        chunk_size=900,
        chunk_overlap=100,
        length_function=len
    )
    return text_splitter.split_text(text)

# Function to create vector store using Hugging Face embeddings
def get_vectorstore(text_chunks, huggingface_api_key):
    embeddings = HuggingFaceEmbeddings(
        model_name="all-MiniLM-L6-v2",
        api_key=huggingface_api_key
    )
    vector_store = FAISS.from_texts(text_chunks, embeddings)
    return vector_store

# Function to initialize conversational chain
def get_conversation_chain(vector_store, api_key):
    llm = ChatOpenAI(openai_api_key=api_key, model_name='gpt-3.5-turbo', temperature=0)
    memory = ConversationBufferMemory(memory_key="chat_history", return_messages=True)
    conversation_chain = ConversationalRetrievalChain.from_llm(
        llm=llm,
        retriever=vector_store.as_retriever(),
        memory=memory
    )
    return conversation_chain

# Handle user input and display conversation
def handle_user_input(user_question):
    with get_openai_callback() as callback:
        response = st.session_state.conversation({"question": user_question})
    st.session_state.chat_history = response["chat_history"]

    # Display chat history
    response_container = st.container()
    with response_container:
        for i, message_obj in enumerate(st.session_state.chat_history):
            if i % 2 == 0:  # User message
                message(message_obj.content, is_user=True, key=str(i))
            else:  # AI response
                message(message_obj.content, key=str(i))

if __name__ == "__main__":
    main()
