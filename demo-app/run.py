import os
import sys
import coverage
import subprocess
import time
from app import app
import flask

cov = coverage.Coverage(config_file=True, source=["app"])

def shutdown():
    """Shutdown the Werkzeug dev server, if we're using it.
    From http://flask.pocoo.org/snippets/67/"""
    func = flask.request.environ.get('werkzeug.server.shutdown')
    if func is None:  # pragma: no cover
        raise RuntimeError('Not running with the Werkzeug Server')
    func()
    print("Server shutting down...")
    return 'Server shutting down...'

if __name__ == "__main__":
		cov.start()
		port = int(os.environ.get("PORT", 5000))
		app.jinja_env.auto_reload = True
		app.config['TEMPLATES_AUTO_RELOAD'] = True
		app.add_url_rule('/shutdown', 'shutdown', shutdown,
											methods=['POST', 'GET'])
		app.run(host='0.0.0.0', port=port)
		cov.stop()
		cov.save()
		time.sleep(2)
		print("Stop get test coverage...")