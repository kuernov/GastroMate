// CustomLineChart.js
import React from 'react';
import { ResponsiveContainer, LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';

const CustomLineChart = ({ data, dataKey, xKey, yLabel, title, color }) => (
    <div style={{ width: '100%', height: 400 }}>
        <h3>{title}</h3> {/* Display the title */}
        <ResponsiveContainer>
            <LineChart data={data} margin={{ top: 20, right: 30, left: 20, bottom: 5 }}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey={xKey} />
                <YAxis label={{ value: yLabel, angle: -90, position: 'insideLeft' }} />
                <Tooltip />
                <Legend />
                <Line type="monotone" dataKey={dataKey} stroke={color} />
            </LineChart>
        </ResponsiveContainer>
    </div>
);

export default CustomLineChart;
