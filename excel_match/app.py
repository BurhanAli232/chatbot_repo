import streamlit as st
import pandas as pd

st.set_page_config(page_title="Excel Smart Search", layout="wide")

st.title("🔍 Excel Smart Search")

try:
    df = pd.read_excel("Open_POs_120425.xlsx")  # UPDATED name here

    query = st.text_input("🔎 Search by keyword (from 'Short Text' column):")

    if query:
        results = df[df['Short Text'].str.contains(query, case=False, na=False)]
        if not results.empty:
            st.success(f"Found {len(results)} matching record(s).")
            st.dataframe(results, use_container_width=True)
        else:
            st.error("No matching records found.")
    else:
        st.info("Type in the search box to find matching records.")

except Exception as e:
    st.error(f"Error loading Excel file: {e}")

st.markdown("---")
st.caption("📄 Powered by Streamlit | Developed by Burhan Ali")
