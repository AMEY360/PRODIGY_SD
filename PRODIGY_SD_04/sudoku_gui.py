import tkinter as tk
from tkinter import messagebox
import random

class SudokuGUI:
    def __init__(self, root):
        self.root = root
        self.root.title("Sudoku Solver")
        self.root.configure(bg="#f2f2f2")
        self.entries = [[None for _ in range(9)] for _ in range(9)]
        self.original_cells = [[False for _ in range(9)] for _ in range(9)]
        self.undo_stack = []
        self.redo_stack = []
        self.hover_highlight = []
        self.selected_cell = (0, 0)  # Default focus
        self.solution_grid = None
        self.build_gui()
        self.bind_keys()

    def build_gui(self):
        for row in range(9):
            for col in range(9):
                e = tk.Entry(self.root, width=3, font=('Helvetica', 18, 'bold'),
                             justify='center', bg='white', fg='black', relief='ridge', borderwidth=2)
                e.grid(row=row, column=col, padx=1, pady=1)
                e.bind("<Enter>", lambda event, r=row, c=col: self.highlight_hover(r, c))
                e.bind("<Leave>", lambda event: self.clear_hover())
                e.bind("<KeyRelease>", lambda event, r=row, c=col: self.track_input(r, c))
                self.entries[row][col] = e

        # Buttons
        style = {'font': ('Helvetica', 12, 'bold'), 'padx': 5, 'pady': 5}
        tk.Button(self.root, text="Solve", command=self.solve_puzzle, bg='#90ee90', **style)\
            .grid(row=9, column=0, columnspan=2, sticky='nsew', padx=2, pady=5)

        tk.Button(self.root, text="Clear", command=self.clear_grid, bg='#ff9999', **style)\
            .grid(row=9, column=2, columnspan=2, sticky='nsew', padx=2, pady=5)

        tk.Button(self.root, text="Undo", command=self.undo, bg='#add8e6', **style)\
            .grid(row=9, column=4, columnspan=1, sticky='nsew', padx=2, pady=5)

        tk.Button(self.root, text="Redo", command=self.redo, bg='#d1b3ff', **style)\
            .grid(row=9, column=5, columnspan=1, sticky='nsew', padx=2, pady=5)

        tk.Button(self.root, text="Hint", command=self.give_hint, bg='#ffff99', **style)\
            .grid(row=9, column=6, columnspan=3, sticky='nsew', padx=2, pady=5)

    def bind_keys(self):
        self.root.bind("<Up>", lambda e: self.move_focus(-1, 0))
        self.root.bind("<Down>", lambda e: self.move_focus(1, 0))
        self.root.bind("<Left>", lambda e: self.move_focus(0, -1))
        self.root.bind("<Right>", lambda e: self.move_focus(0, 1))
        self.focus_cell(0, 0)

    def move_focus(self, dr, dc):
        row, col = self.selected_cell
        new_row = max(0, min(8, row + dr))
        new_col = max(0, min(8, col + dc))
        self.focus_cell(new_row, new_col)

    def focus_cell(self, row, col):
        self.selected_cell = (row, col)
        self.entries[row][col].focus_set()

    def track_input(self, row, col):
        val = self.entries[row][col].get()
        self.undo_stack.append((row, col, val))
        self.redo_stack.clear()

    def undo(self):
        if self.undo_stack:
            row, col, _ = self.undo_stack.pop()
            prev_val = self.entries[row][col].get()
            self.entries[row][col].delete(0, tk.END)
            self.entries[row][col].insert(0, '')
            self.redo_stack.append((row, col, prev_val))

    def redo(self):
        if self.redo_stack:
            row, col, val = self.redo_stack.pop()
            self.entries[row][col].delete(0, tk.END)
            self.entries[row][col].insert(0, val)
            self.undo_stack.append((row, col, val))

    def highlight_hover(self, row, col):
        self.clear_hover()
        for i in range(9):
            self.entries[row][i].config(bg='#e0ffff')
            self.entries[i][col].config(bg='#e0ffff')
            self.hover_highlight.append((row, i))
            self.hover_highlight.append((i, col))

    def clear_hover(self):
        for row in range(9):
            for col in range(9):
                if self.original_cells[row][col]:
                    self.entries[row][col].config(bg='#e6f7ff')
                else:
                    self.entries[row][col].config(bg='white')
        self.hover_highlight = []

    def read_grid(self):
        grid = []
        valid = True
        for row in range(9):
            current_row = []
            for col in range(9):
                val = self.entries[row][col].get().strip()
                self.entries[row][col].config(bg='white')
                if val == "":
                    current_row.append(0)
                else:
                    try:
                        num = int(val)
                        if 1 <= num <= 9:
                            current_row.append(num)
                            self.original_cells[row][col] = True
                            self.entries[row][col].config(bg="#e6f7ff")
                        else:
                            raise ValueError
                    except ValueError:
                        self.entries[row][col].config(bg='lightcoral')
                        valid = False
                        break
            grid.append(current_row)
        return grid if valid else None

    def fill_grid(self, grid):
        for row in range(9):
            for col in range(9):
                if not self.original_cells[row][col]:
                    self.entries[row][col].delete(0, tk.END)
                    self.entries[row][col].insert(0, str(grid[row][col]))
                    self.entries[row][col].config(bg="#e6ffe6")

    def clear_grid(self):
        for row in range(9):
            for col in range(9):
                self.entries[row][col].delete(0, tk.END)
                self.entries[row][col].config(bg='white')
                self.original_cells[row][col] = False
        self.undo_stack.clear()
        self.redo_stack.clear()
        self.solution_grid = None

    def is_valid(self, grid, row, col, num):
        for i in range(9):
            if grid[row][i] == num or grid[i][col] == num:
                return False
        start_row, start_col = 3 * (row // 3), 3 * (col // 3)
        for i in range(start_row, start_row + 3):
            for j in range(start_col, start_col + 3):
                if grid[i][j] == num:
                    return False
        return True

    def solve(self, grid):
        for row in range(9):
            for col in range(9):
                if grid[row][col] == 0:
                    for num in range(1, 10):
                        if self.is_valid(grid, row, col, num):
                            grid[row][col] = num
                            if self.solve(grid):
                                return True
                            grid[row][col] = 0
                    return False
        return True

    def solve_puzzle(self):
        self.original_cells = [[False for _ in range(9)] for _ in range(9)]
        grid = self.read_grid()
        if grid is not None:
            self.solution_grid = [row[:] for row in grid]
            if self.solve(self.solution_grid):
                self.fill_grid(self.solution_grid)
            else:
                messagebox.showinfo("Sudoku Solver", "No solution exists for the given puzzle.")

    def give_hint(self):
        if not self.solution_grid:
            grid = self.read_grid()
            if grid is None:
                return
            self.solution_grid = [row[:] for row in grid]
            if not self.solve(self.solution_grid):
                messagebox.showinfo("Hint", "Cannot provide a hint — puzzle is unsolvable.")
                return

        # Find empty cells
        empty_cells = [(r, c) for r in range(9) for c in range(9)
                       if not self.original_cells[r][c] and self.entries[r][c].get().strip() == '']
        if empty_cells:
            r, c = random.choice(empty_cells)
            self.entries[r][c].insert(0, str(self.solution_grid[r][c]))
            self.entries[r][c].config(bg="#ffffcc")
            self.original_cells[r][c] = True
        else:
            messagebox.showinfo("Hint", "No more hints available — puzzle is already filled.")

# Run the GUI
if __name__ == "__main__":
    root = tk.Tk()
    app = SudokuGUI(root)
    root.mainloop()
