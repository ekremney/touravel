from flask import request, jsonify

def render_response(status_code, message_dict):
	response = jsonify(message_dict)
	response.status_code = status_code
	return response