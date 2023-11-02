import os
import re


original_file_path = "D:/imad-server/src/main/java/com/ncookie/imad/global/dto/response/ResponseCode.java"
csv_file_path = "D:/imad-server/document/response_code.csv"
pattern = r'(\w+)\((\d+),\s+"([^"]+)"\)'

response_code_csv = ""
with open(original_file_path, "r", encoding="utf-8") as response_code:

    for line in response_code.readlines():
        match = re.search(pattern, line)

        # Extract the matched groups
        if match:
            name = match.group(1)
            code = match.group(2)
            message = match.group(3)

            response_code_csv += f"{code}, \"{name}\", \"{message}\""
            response_code_csv += "\n"

    print("RESPONSE CODE 추출 완료")

with open(csv_file_path, "w", encoding="utf-8") as csv_file:
    print("CSV 파일 작성 완료")
    csv_file.write(response_code_csv)
