import xml.etree.ElementTree as ET
import base64
import json
import sys
import time
import os

source_path = os.path.join((os.path.dirname(os.path.realpath(__file__))),"app")

def get_impact_test(path):
  coverage_tree = ET.parse(path)
  root = coverage_tree.getroot()
  packages_element = root.find("packages")
  class_elements = packages_element.findall("package/classes/class")
  impact_test = {}
  for class_element in class_elements:
    get_impact_source_file(class_element, impact_test)
  return impact_test

def get_impact_source_file(class_element, impact_test):
  class_name = class_element.attrib['name']
  source_file_token = __token_source_file(class_element.attrib['filename'])
  class_name_encode = __decode_class_name(class_name)
  impact_test[class_name_encode]= {"file_name": class_element.attrib['filename'], "impact_lines": [], "impact_methods": []}

  impact_test[class_name_encode]['file_name'] = class_element.attrib['filename']
  current_state = 0
  start_line = 0
  end_line = 0
  for lines in class_element.find("lines").getchildren():
    method_name, (impact_loc, start_line, end_line, current_state) = get_impact_method(source_file_token, class_name_encode, lines, current_state, start_line, end_line)
    impact_loc is not None and impact_test[class_name_encode]["impact_lines"].append(impact_loc)
    method_name is not None and impact_test[class_name_encode]["impact_methods"].append(method_name)
  is_impact_latest_method = current_state is "1"
  if is_impact_latest_method:
    impact_test[class_name_encode]['impact_lines'].append(f"{start_line}-{end_line}")
  if len(impact_test[class_name_encode]['impact_lines']) == 0:
    del impact_test[class_name_encode]

def get_impact_method(source_file_token, class_name, lines, current_state, start_line, end_line):
    impact_line = None
    method_name = None
    is_change_state = int(current_state) is not int(lines.attrib['hits'])
    is_coverage_method = int(lines.attrib['hits']) is 1
    is_firt_start_line = int(start_line) is 0
    is_ingore_source_code_line = is_change_state and is_coverage_method and not is_firt_start_line
    is_export_impact_method = is_change_state and not is_coverage_method and not is_firt_start_line
    is_begin_get_impact_source_code = is_change_state and is_coverage_method and is_firt_start_line
    is_continue_count_coverage_method = not is_change_state and is_coverage_method
    is_ingore_uncover_method = not is_change_state and not is_coverage_method
    is_coverage_line = int(lines.attrib['hits']) is 1
    if is_coverage_line:
      method_name = __get_impact_method_name(lines, source_file_token)
    if is_ingore_uncover_method:
      return [method_name, __ingore_uncover_method_handler(lines, current_state, start_line, end_line)]
    if is_continue_count_coverage_method:
      return [method_name, __continue_count_coverage_method_handler(lines, current_state, start_line, end_line)]
    if is_ingore_source_code_line:
      return [method_name, __ingore_source_code_line_method_handler(lines, current_state, start_line, end_line)]
    if is_export_impact_method:
      return [method_name, __export_impact_method_handler(lines, current_state, start_line, end_line)]
    if is_begin_get_impact_source_code:
      return [method_name, __begin_get_impact_source_code_handler(lines, current_state, start_line, end_line)]
    return method_name, impact_line, start_line, end_line, current_state

def __decode_class_name(class_name):
  class_name = class_name + str(time.time())
  class_name_encode = base64.b64encode(class_name.encode("utf-8"))
  return str(class_name_encode,"utf-8") 
  
def __token_source_file(file_name):
  source_file = open(os.path.join(source_path,file_name),'r')
  source_content = source_file.read()
  return [line.strip() for line in source_content.split("\n")]

def __ingore_uncover_method_handler(lines, current_state, start_line, end_line):
  impact_line = None
  start_line = lines.attrib['number']
  current_state = lines.attrib['hits']
  return impact_line, start_line, end_line, current_state

def __continue_count_coverage_method_handler(lines, current_state, start_line, end_line):
  impact_line = None
  end_line = lines.attrib['number']
  current_state = lines.attrib['hits']
  return impact_line, start_line, end_line, current_state

def __ingore_source_code_line_method_handler(lines, current_state, start_line, end_line):
  impact_line = None
  start_line = lines.attrib['number']
  end_line = lines.attrib['number']
  current_state = lines.attrib['hits']
  return impact_line, start_line, end_line, current_state

def __export_impact_method_handler(lines, current_state, start_line, end_line):
  impact_line = None if start_line is 0 and int(current_state) is 1 else f"{start_line}-{int(end_line)}"
  start_line = lines.attrib['number']
  end_line = lines.attrib['number']
  current_state = lines.attrib['hits']
  return impact_line, start_line, end_line, current_state

def __begin_get_impact_source_code_handler(lines, current_state, start_line, end_line):
  impact_line = None
  start_line = 1
  end_line = lines.attrib['number']
  current_state = lines.attrib['hits']
  return impact_line, start_line, end_line, current_state

def __get_impact_method_name(lines, source_file_token):
  source_line_number = int(lines.attrib['number'])-1
  souce_line = source_file_token[source_line_number]
  is_static_or_decorate_method = lambda: "@" in souce_line
  while is_static_or_decorate_method():
    source_line_number +=1
    souce_line = source_file_token[source_line_number]
  is_method_line = "def" in souce_line
  if is_method_line:
    return str(souce_line).replace("def","").replace(":","").strip()

if __name__ == "__main__":
  file_path = sys.argv[1] if len(sys.argv) > 1 else None
  if not file_path:
    raise Exception('Missing cov file for analyze impact test')
  is_directory_path = file_path and os.path.isdir(file_path)
  if is_directory_path:
    mapping_impact_folder = os.path.join(file_path,"impact-mapping")
    not os.path.exists(mapping_impact_folder) and os.makedirs(mapping_impact_folder)
    for root, dirs, files in os.walk(file_path):
      for file in files:
        file_extension = file.split('.')[1]
        if "xml" == file_extension:
          file_mapping_name = f'{file.split(".")[0]}.json'
          impact_test = get_impact_test(os.path.join(root,file))
          with open(os.path.join(mapping_impact_folder, file_mapping_name),"w") as impact_file:
            impact_file.write(json.dumps(impact_test))
  else:
    mapping_impact_folder = os.path.join(os.path.dirname(file_path),"impact-mapping")
    file_mapping_name = f'{(os.path.basename(file_path)).split(".")[0]}.json'
    not os.path.exists(mapping_impact_folder) and os.makedirs(mapping_impact_folder)
    impact_test = get_impact_test(file_path)
    with open(os.path.join(mapping_impact_folder, file_mapping_name),"w") as impact_file:
      impact_file.write(json.dumps(impact_test))
  print('Done generate test impact mapping...')