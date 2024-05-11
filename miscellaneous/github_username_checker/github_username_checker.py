import requests

def check_github_username(username):
    url = f"https://api.github.com/users/{username}"
    response = requests.get(url)
    if response.status_code == 404:
        return True  # Username is available
    elif response.status_code == 200:
        return False  # Username is taken
    else:
        print("Error checking username:", response.status_code)
        return None

if __name__ == "__main__":
    print("Enter GitHub usernames to check their availability. Type 'exit' to quit.")
    while True:
        username = input("Enter GitHub username: ")
        if username.lower() == 'exit':
            break
        is_available = check_github_username(username)
        if is_available is None:
            print("An error occurred. Please try again.")
        elif is_available:
            print(f"The username '{username}' is available.")
        else:
            print(f"The username '{username}' is taken.")
