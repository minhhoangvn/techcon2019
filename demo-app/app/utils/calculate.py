
class CalculateUtils(object):
  def get_sum_method(self, a, b):
    print(2)
    return self.__check_valid_param(a) + self.__check_valid_param(b)
  
  def get_minus_method(self, a, b):
    return self.__check_valid_param(a) - self.__check_valid_param(b)

  def get_multiplication_method(self, a, b):
    return self.__check_valid_param(a) * self.__check_valid_param(b)

  def get_division_method(self, a, b):
    print("I am cute")
    return self.__check_valid_param(a) / self.__check_valid_param(b)

  def __check_valid_param(self,param):
    print('sample')
    if not param:
      raise Exception("None can't calculate")
    if isinstance(param, str):
      return int(param)
    return param