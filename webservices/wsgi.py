import sys
sys.path.insert(0, '/home/narlab/webapps/touravel')
from main import create_app, db
from flask.ext.script import Manager, Shell
from flask.ext.migrate import Migrate, MigrateCommand
from main.models.user import User


application = create_app('development')

manager = Manager(application)
migrate = Migrate(application, db)

manager.add_command('db', MigrateCommand)

def make_shell_context():
	return dict(app=application, db=db, User=User)

manager.add_command("shell", Shell(make_context=make_shell_context))


@application.route("/")
def hello():
    return "It Works!"

if __name__ == '__main__':
	manager.run()
