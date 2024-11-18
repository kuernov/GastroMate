import sys
import json
from statsmodels.tsa.statespace.sarimax import SARIMAX
import pmdarima as pm

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

if __name__ == "__main__":
    # Odczyt danych wejściowych jako JSON
    data = json.loads(sys.argv[1])

    # Parametry wejściowe
    auto_tune = sys.argv[2].lower() == 'true'
    s = int(sys.argv[3])
    steps = int(sys.argv[4])

    # Dopasowanie modelu SARIMA
    if auto_tune:
        (p, d, q), (P, D, Q, _) = auto_tune_sarima(data, s)
    else:
        p = int(sys.argv[5])
        d = int(sys.argv[6])
        q = int(sys.argv[7])
        P = int(sys.argv[8])
        D = int(sys.argv[9])
        Q = int(sys.argv[10])

    # Prognoza
    result = predict(data, p, d, q, P, D, Q, s, steps)

    # Zwracanie wyników w formacie JSON
    output = {
        "forecast": result,
        "selected_order": [p, d, q],
        "selected_seasonal_order": [P, D, Q, s]
    }
    print(json.dumps(output))
