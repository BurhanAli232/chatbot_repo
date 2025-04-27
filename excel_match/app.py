import streamlit as st
import pandas as pd

# Page configuration
st.set_page_config(page_title="Excel Smart Search", layout="wide")

# Title
st.title("🔍 Excel Smart Search")

# Load the Excel file
@st.cache_data
def load_data():
    return pd.read_excel("Open POs 120425.xlsx")  # Make sure this file is uploaded too

df = load_data()

# Search box
query = st.text_input("🔎 Search by keyword:")

# Display matching results
if query:
    matches = df[df['Short Text'].str.contains(query, case=False, na=False)]
    st.dataframe(matches, use_container_width=True)
