from flask import Flask
from flask.ext.sqlalchemy import SQLAlchemy

class Config:
    SECRET_KEY = 'hard to guess string'
    SQLALCHEMY_COMMIT_ON_TEARDOWN = True
    SQLALCHEMY_DATABASE_URI = 'postgresql://touravel_admin:1111@localhost/touravel'

    @staticmethod
    def init_app(app):
        pass

class DevelopmentConfig(Config):
    DEBUG = True


class TestingConfig(Config):
    TESTING = True


class ProductionConfig(Config):
    TESTING = False

config = {
    'default': DevelopmentConfig
}

db = SQLAlchemy()

def create_app(config_name):
    app = Flask(__name__)
    app.config.from_object(config[config_name])
    config[config_name].init_app(app)

    db.init_app(app)
    
    from .api import api as api_blueprint
    app.register_blueprint(api_blueprint)
    
    return app
