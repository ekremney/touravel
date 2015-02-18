#!/usr/bin/env python
from main import create_app, db
from flask.ext.script import Manager, Shell
from flask.ext.migrate import Migrate, MigrateCommand
from main.models.user import User

app = create_app('development')

manager = Manager(app)
migrate = Migrate(app, db)

manager.add_command('db', MigrateCommand)

def make_shell_context():
	return dict(app=app, db=db, User=User)

manager.add_command("shell", Shell(make_context=make_shell_context))

@app.route("/")
def hello():
    return "Hello World yep!"

if __name__ == '__main__':
    manager.run()
