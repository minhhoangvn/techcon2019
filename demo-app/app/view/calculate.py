from flask import Blueprint, jsonify, render_template, request
from app.utils.calculate import CalculateUtils

calculate_view = Blueprint("calculate_view",__name__)

@calculate_view.route('/calculate', methods = ['GET', 'POST'])
def calculate_process():
   return render_template('calculate.html')

@calculate_view.route('/calculate/addition', methods = ['GET', 'POST'])
def calculate_addition_process():
   calculate = CalculateUtils()
   json_data = request.get_json()
   number_a = json_data['number-a']
   number_b = json_data['number-b']
   return jsonify({
      "number-a": number_a,
      "number-b": number_b,
      "result": calculate.get_sum_method(number_a, number_b)
   })

@calculate_view.route('/calculate/subtraction', methods = ['GET', 'POST'])
def calculate_subtraction_process():
   calculate = CalculateUtils()
   json_data = request.get_json()
   number_a = json_data['number-a']
   number_b = json_data['number-b']
   return jsonify({
      "number-a": number_a,
      "number-b": number_b,
      "result": calculate.get_minus_method(number_a, number_b)
   })


@calculate_view.route('/calculate/multiplication', methods = ['GET', 'POST'])
def calculate_multiplication_process():
   calculate = CalculateUtils()
   json_data = request.get_json()
   number_a = json_data['number-a']
   number_b = json_data['number-b']
   return jsonify({
      "number-a": number_a,
      "number-b": number_b,
      "result": calculate.get_multiplication_method(number_a, number_b)
   })

@calculate_view.route('/calculate/division', methods = ['GET', 'POST'])
def calculate_division_process():
   calculate = CalculateUtils()
   json_data = request.get_json()
   number_a = json_data['number-a']
   number_b = json_data['number-b']
   return jsonify({
      "number-a": number_a,
      "number-b": number_b,
      "result": calculate.get_division_method(number_a, number_b)
   })