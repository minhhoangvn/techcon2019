from flask import Flask
from .view.calculate import calculate_view
from .view.processinfo import process_info_view

app = Flask(__name__)
app.register_blueprint(process_info_view)
app.register_blueprint(calculate_view)

