import sys

def caesar_cipher(text, shift):
    result = ""
    for char in text:
        if char.isalpha():
            ascii_offset = 65 if char.isupper() else 97
            result += chr((ord(char) - ascii_offset + shift) % 26 + ascii_offset)
        else:
            result += char
    return result

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python caesar_cipher.py <text>")
        sys.exit(1)

    text = sys.argv[1]

    for shift in range(26):
        encoded_text = caesar_cipher(text, shift)
        print(f"Shift {shift}: {encoded_text}")
