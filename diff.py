import json
import os

def get_diff_methods():
  diff_lines = []
  diff_methods = {}
  current_file_diff = ""
  current_line_number = 0
  with open("diff_lines.txt","r") as diff_file:
    diff_lines = diff_file.read().split("\n")
  for line in diff_lines:
    is_diff_file_line = "diff --git" in line
    if is_diff_file_line:
      current_file_diff = __get_diff_file_name(diff_methods ,line, current_file_diff)
    else:
      __get_diff_method_name(diff_methods, diff_lines, current_line_number, current_file_diff)
    current_line_number +=1
  return diff_methods

def __is_reach_line_limit(diff_lines, current_line_number):
  return current_line_number == len(diff_lines)

def __get_diff_file_name(diff_methods, line, current_file_diff):
    diff_file_name = line.replace("diff --git","").strip()
    diff_file_name = diff_file_name.split(" ")
    diff_file_name = diff_file_name[len(diff_file_name)-1]
    diff_file_name = diff_file_name[2:]
    current_file_diff = diff_file_name
    diff_methods[current_file_diff] = {}
    return current_file_diff

def __get_diff_method_name(diff_methods, diff_lines, current_line_number, current_file_diff):
  if __is_reach_line_limit(diff_lines, current_line_number+1):
    return
  current_line = diff_lines[current_line_number]
  next_line = diff_lines[current_line_number + 1]
  if "method_name" not in diff_methods[current_file_diff]:
    diff_methods[current_file_diff] = {"method_name": []}
  is_change_in_method_code_block = lambda line: line.startswith("-")
  is_reach_next_method_name = lambda line: "def" in line
  if "def" in current_line:
    while not is_reach_next_method_name(next_line):
      if is_change_in_method_code_block(next_line):
        is_exist_in_list_impact_method = __filter_method_name(current_line) not in diff_methods[current_file_diff]["method_name"] 
        is_exist_in_list_impact_method and diff_methods[current_file_diff]["method_name"].append(__filter_method_name(current_line))
      current_line_number += 1
      next_line = "def" if __is_reach_line_limit(diff_lines, current_line_number) else diff_lines[current_line_number]

def __filter_method_name(line):
  if line.startswith("-"):
    line = line[1:]
    line = line.strip()
  else:
    line = line.strip()
  line = line.replace(":","")
  line = line.replace("def","")
  return line.strip()

if __name__ == "__main__":
    diff_methods = get_diff_methods()
    file_path = os.path.join(os.path.dirname(os.path.realpath(__file__)),"diff_methods.json")
    with open(file_path,"w") as diff_methods_file:
      diff_methods_file.write(json.dumps(diff_methods))