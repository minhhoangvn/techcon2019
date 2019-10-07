import cpuinfo
import psutil
import platform
import datetime

class SystemInfoUtils(object):
  @staticmethod
  def get_cpu_info():
    return cpuinfo.get_cpu_info()
  
  @staticmethod
  def get_virtual_memory_info():
    return psutil.virtual_memory()
  
  @staticmethod
  def get_net_info():
    return psutil.net_if_addrs()
  
  @staticmethod
  def get_boot_time_info():
    return datetime.datetime.fromtimestamp(psutil.boot_time()).strftime("%Y-%m-%d %H:%M:%S")