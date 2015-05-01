import sys
sys.path.insert(0, '/home/narlab/webapps/touravel')
from main import create_app
application = create_app('development')


@application.route("/")
def hello():
    return "It Works!"

if __name__ == '__main__':
	application.run()
