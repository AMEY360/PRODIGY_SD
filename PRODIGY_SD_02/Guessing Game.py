import tkinter as tk
import random
from tkinter import messagebox

class GuessingGame:
    def __init__(self, root):
        self.root = root
        self.root.title("🎯 Number Guessing Game")
        self.root.geometry("480x400")
        self.root.configure(bg="#f9f9f9")

        self.attempts = 0
        self.animation_steps = 10
        self.create_widgets()

    def create_widgets(self):
        self.title_label = tk.Label(self.root, text="🎯 Guess the Number (1-100)",
                                    font=("Helvetica", 18, "bold"), bg="#f9f9f9", fg="#222")
        self.title_label.pack(pady=15)

        self.guess_entry = tk.Entry(self.root, font=("Helvetica", 16), justify='center')
        self.guess_entry.pack(pady=10)

        self.submit_button = tk.Button(self.root, text="Submit Guess", font=("Helvetica", 13, "bold"),
                                       bg="#4CAF50", fg="white", width=15, command=self.check_guess)
        self.submit_button.pack(pady=10)

        self.result_frame = tk.Frame(self.root, bg="#f9f9f9")
        self.result_frame.pack(pady=15)

        self.user_guess_label = tk.Label(self.result_frame, text="", font=("Helvetica", 13), bg="#f9f9f9")
        self.generated_number_label = tk.Label(self.result_frame, text="", font=("Helvetica", 13), bg="#f9f9f9")
        self.match_result_label = tk.Label(self.result_frame, text="", font=("Helvetica", 14, "bold"), bg="#f9f9f9")

        self.user_guess_label.pack()
        self.generated_number_label.pack()
        self.match_result_label.pack()

        self.attempts_label = tk.Label(self.root, text="Attempts: 0", font=("Helvetica", 12), bg="#f9f9f9")
        self.attempts_label.pack()

        self.reset_button = tk.Button(self.root, text="Reset Game", font=("Helvetica", 12, "bold"),
                                      bg="#f44336", fg="white", width=15, command=self.reset_game)
        self.reset_button.pack(pady=5)

    def animate_text(self, label, full_text, step=0):
        label.config(text=full_text[:step])
        if step <= len(full_text):
            self.root.after(40, lambda: self.animate_text(label, full_text, step + 1))

    def check_guess(self):
        guess = self.guess_entry.get()
        if not guess.isdigit():
            messagebox.showwarning("Invalid Input", "Please enter a valid number between 1 and 100.")
            return

        guess = int(guess)
        generated = random.randint(1, 100)
        self.attempts += 1

        # Clear previous text
        self.user_guess_label.config(text="")
        self.generated_number_label.config(text="")
        self.match_result_label.config(text="")
        self.attempts_label.config(text=f"Attempts: {self.attempts}")

        self.animate_text(self.user_guess_label, f"Your Guess: {guess}")
        self.animate_text(self.generated_number_label, f"Generated Number: {generated}")

        if guess == generated:
            self.animate_text(self.match_result_label, "✅ It's a MATCH!")
            self.flash_button(self.submit_button)
            self.submit_button.config(state='disabled')
            self.root.after(1000, lambda: messagebox.showinfo("Congratulations!",
                                                              f"You guessed it right in {self.attempts} attempts!"))
        else:
            self.animate_text(self.match_result_label, "❌ Not a match. Try again!")

        self.guess_entry.delete(0, tk.END)

    def flash_button(self, button, flash_count=6):
        def toggle_color(count):
            if count == 0:
                button.config(bg="#4CAF50")
                return
            current = button.cget("bg")
            new_color = "#A5D6A7" if current == "#4CAF50" else "#4CAF50"
            button.config(bg=new_color)
            self.root.after(100, lambda: toggle_color(count - 1))
        toggle_color(flash_count)

    def reset_game(self):
        self.attempts = 0
        self.guess_entry.delete(0, tk.END)
        self.user_guess_label.config(text="")
        self.generated_number_label.config(text="")
        self.match_result_label.config(text="")
        self.attempts_label.config(text="Attempts: 0")
        self.submit_button.config(state='normal')
        self.submit_button.config(bg="#4CAF50")

# Run the game
if __name__ == "__main__":
    root = tk.Tk()
    app = GuessingGame(root)
    root.mainloop()
