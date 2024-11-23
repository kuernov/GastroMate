import {BarChart, Bar, XAxis, YAxis, Tooltip, Legend, ResponsiveContainer, CartesianGrid} from 'recharts';

const CategoryQuantityChart = ({ data }) => (
    <ResponsiveContainer width="100%" height={400}>
        <BarChart data={data}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="categoryName" />
            <YAxis />
            <Tooltip />
            <Legend />
            <Bar dataKey="quantity" fill="#8b5cf6" />
        </BarChart>
    </ResponsiveContainer>
);
export default CategoryQuantityChart;