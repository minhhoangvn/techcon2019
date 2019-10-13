import json

def get_impacted_test():
  impacted_tests = []
  with open("./diff_methods.json","r") as impacted_methods:
    impacted_methods = json.loads(impacted_methods.read())
  with open("./final_mapping_result.json","r") as mapping_methods:
    mapping_methods = json.loads(mapping_methods.read())
  for soure_file, impact_info in impacted_methods.items():
    for file, mapping_info in mapping_methods.items():
      if soure_file in mapping_info['file_name']:
        for method in impact_info['method_name']:
          impacted_tests.extend(mapping_info[method]['test_impact'])
  return impacted_tests

if __name__ == "__main__":
  impacted_tests = get_impacted_test()
  with open('impacted_test.txt','w') as impacted_test_file:
    for test in impacted_tests:
      impacted_test_file.write(test+"\n")