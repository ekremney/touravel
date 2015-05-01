#!/usr/bin/env python
from wsgi import application as app
from main import db
from flask.ext.script import Manager, Shell
from flask.ext.migrate import Migrate, MigrateCommand
from main.models.user import User

manager = Manager(app)
migrate = Migrate(app, db)

manager.add_command('db', MigrateCommand)

def make_shell_context():
	return dict(app=app, db=db, User=User)

manager.add_command("shell", Shell(make_context=make_shell_context))


if __name__ == '__main__':
    manager.run()
