import {BarChart, Bar, XAxis, YAxis, Tooltip, Legend, ResponsiveContainer, CartesianGrid} from 'recharts';

const CategoryRevenueChart = ({ data }) => (
    <ResponsiveContainer width="100%" height={400}>
        <BarChart data={data}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="categoryName" />
            <YAxis />
            <Tooltip />
            <Legend />
            <Bar dataKey="totalRevenue" fill="#2563eb" />
        </BarChart>
    </ResponsiveContainer>
);
 export default CategoryRevenueChart;