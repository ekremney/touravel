from flask import request, jsonify
from .. import db
from main.models.user import User
from . import api
from .errors import bad_request, unauthorized, forbidden

@api.route('/api/v1.0/user', methods=['POST'])
def register():
	user = User.from_json(request.json)
	response = jsonify({'message': 'User is successfully created.'})
	response.status_code = 201
	return response

@api.route('/api/v1.0/user', methods=['GET'])
def get_users():
	return jsonify(request.headers)
	