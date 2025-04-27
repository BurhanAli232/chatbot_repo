import streamlit as st
import pandas as pd

# Page configuration
st.set_page_config(
    page_title="Excel Smart Search",
    page_icon="🔍",
    layout="wide"
)

# Title
st.title("🔍 Excel Smart Search")

# Load Excel Data
@st.cache_data
def load_data():
    try:
        data = pd.read_excel("Open POs 120425.xlsx")  # Make sure this Excel file is uploaded
        return data
    except Exception as e:
        st.error(f"Error loading Excel file: {e}")
        return pd.DataFrame()

# Load the data
df = load_data()

# Check if data is loaded
if df.empty:
    st.warning("No data found. Please upload a valid Excel file named 'Open POs 120425.xlsx'.")
else:
    # Search box
    query = st.text_input("🔎 Search by keyword (from 'Short Text' column):")

    # Show results dynamically
    if query:
        results = df[df['Short Text'].str.contains(query, case=False, na=False)]
        if not results.empty:
            st.success(f"Found {len(results)} matching record(s).")
            st.dataframe(results, use_container_width=True)
        else:
            st.error("No matching records found.")
    else:
        st.info("Type in the search box to find matching records.")

# Footer
st.markdown("---")
st.caption("📄 Powered by Streamlit | Developed by Burhan Ali")
