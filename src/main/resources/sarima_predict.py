from flask import Flask, request, jsonify
from statsmodels.tsa.statespace.sarimax import SARIMAX
import pmdarima as pm

app = Flask(__name__)

def auto_tune_sarima(data, seasonal_period):
    stepwise_fit = pm.auto_arima(
        data,
        seasonal=True,
        m=seasonal_period,
        trace=False,
        error_action='ignore',
        suppress_warnings=True,
        stepwise=True
    )
    return stepwise_fit.order, stepwise_fit.seasonal_order

def predict(data, p, d, q, P, D, Q, s, steps):
    model = SARIMAX(data, order=(p, d, q), seasonal_order=(P, D, Q, s))
    fitted_model = model.fit(disp=False)
    forecast = fitted_model.forecast(steps=steps)
    return forecast.tolist()

@app.route('/predict', methods=['POST'])
def sarima_predict():
    try:
        # Odczyt danych wejściowych
        input_data = request.get_json()
        data = input_data.get("data", [])
        auto_tune = input_data.get("auto_tune", True)
        seasonal_period = input_data.get("seasonal_period", 7)
        steps = input_data.get("steps", 7)

        # Walidacja danych wejściowych
        if not data:
            return jsonify({"error": "No data provided"}), 400

        # Dopasowanie modelu SARIMA
        if auto_tune:
            (p, d, q), (P, D, Q, _) = auto_tune_sarima(data, seasonal_period)
        else:
            p = 1
            d = 0
            q = 0
            P = 0
            D = 0
            Q = 1

        # Prognoza
        forecast = predict(data, p, d, q, P, D, Q, seasonal_period, steps)

        # Zwracanie wyników
        output = {
            "forecast": forecast,
            "selected_order": [p, d, q],
            "selected_seasonal_order": [P, D, Q, seasonal_period]
        }
        return jsonify(output)

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
