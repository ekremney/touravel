from flask import jsonify
from app.exceptions import ValidationError
from . import api

def bad_request(message):
	response = jsonify({'error': 'Bad Request', 'message': message})
	response.status_code = 400
	return response

def unauthorized(message):
	response = jsonify({'error': 'Unauthorized', 'message': message})
	response.status_code = 401
	return response

def forbidden(message):
	response = jsonify({'error': 'Forbidden', 'message': message})
	response.status_code = 403
	return response

@api.errorhandler(ValidationError)
def validation_error(e):
	return bad_request(e.args[0])

@api.errorhandler(400)
def bad_request_generic(message):
	response = jsonify({'error': 'Bad Request', 'message': 'The client (or proxy) sent a request that this server could not understand.'})
	response.status_code = 400
	return response
