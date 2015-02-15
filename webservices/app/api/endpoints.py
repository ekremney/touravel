from flask import request, jsonify
from .. import db
from ..models import User
from . import api
from .errors import bad_request, unauthorized, forbidden

@api.route('/', methods=['GET'])
def index():
	return jsonify(request.headers)

@api.route('/api/v1.0/user', methods=['POST'])
def register():
	user = User.from_json(request.json)
	return str(request.json)

@api.route('/api/v1.0/user', methods=['GET'])
def get_users():
	return jsonify(request.headers)