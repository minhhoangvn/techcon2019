from flask import Blueprint, jsonify, render_template
from app.utils.utils import SystemInfoUtils
import cpuinfo
import psutil
import platform
import datetime
process_info_view = Blueprint("process_info_view",__name__)

@process_info_view.route('/')
def index():
   return render_template('index.html')

@process_info_view.route('/info')
def info():
   osinfo = {}
   osinfo['plat'] = platform
   osinfo['cpu'] = SystemInfoUtils.get_cpu_info()
   osinfo['mem'] = SystemInfoUtils.get_virtual_memory_info()
   osinfo['net'] = SystemInfoUtils.get_net_info()
   osinfo['boottime'] = SystemInfoUtils.get_boot_time_info()

   return render_template('info.html', info = osinfo)

@process_info_view.route('/monitor')
def monitor():
   return render_template('monitor.html')

olddata = {}
olddata['disk_write'] = 0
olddata['disk_read'] = 0
olddata['net_sent'] = 0
olddata['net_recv'] = 0
@process_info_view.route('/api/monitor')
def api_monitor():
   apidata = {}
   apidata['cpu'] = psutil.cpu_percent(interval=0.9)
   apidata['mem'] = psutil.virtual_memory().percent
   apidata['disk'] = psutil.disk_usage('/').percent
   
   try:
      netio = psutil.net_io_counters()
      apidata['net_sent'] = 0 if olddata['net_sent'] == 0 else netio.bytes_sent - olddata['net_sent']
      olddata['net_sent'] = netio.bytes_sent
      apidata['net_recv'] = 0 if olddata['net_recv'] == 0 else netio.bytes_recv - olddata['net_recv']
      olddata['net_recv'] = netio.bytes_recv
   except:
      apidata['net_sent'] = -1
      apidata['net_recv'] = -1
      
   try:
      diskio = psutil.disk_io_counters()
      apidata['disk_write'] = 0 if olddata['disk_write'] == 0 else diskio.write_bytes - olddata['disk_write']
      olddata['disk_write'] = diskio.write_bytes
      apidata['disk_read'] = 0 if olddata['disk_read'] == 0 else diskio.read_bytes - olddata['disk_read']
      olddata['disk_read'] = diskio.read_bytes
   except:
      apidata['disk_write'] = -1
      apidata['disk_read'] = -1

   return jsonify(apidata)

@process_info_view.route('/api/process')
def api_process():
   apidata = {}
   try:
      apidata['processes'] = []
      for proc in psutil.process_iter():
         try:
            #pinfo = proc.as_dict(attrs=['pid', 'name', 'num_handles', 'num_threads', 'memory_percent', 'cpu_times'])
            pinfo = proc.as_dict(attrs=['pid', 'name', 'memory_percent', 'num_threads', 'cpu_times'])
         except psutil.NoSuchProcess:
            pass
         else:
            apidata['processes'].append(pinfo)
   except:
      pass

   return jsonify(apidata)