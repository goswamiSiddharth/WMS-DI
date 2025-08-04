import React, { useEffect, useState } from 'react';
import { Bar } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
} from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

export default function MskuChart() {
  const [summary, setSummary] = useState(null);

  useEffect(() => {
    fetch('/api/skus')
      .then(res => res.json())
      .then(data => setSummary(data))
      .catch(err => console.error('Failed to fetch summary:', err));
  }, []);

  if (!summary) return null;

  const chartData = {
    labels: ['Mapped SKUs', 'New SKUs'],
    datasets: [
      {
        label: '# of SKUs',
        data: [summary.mappedCount, summary.newCount],
        backgroundColor: ['rgba(75, 192, 192, 0.6)', 'rgba(255, 159, 64, 0.6)'],
      }
    ],
  };

  return (
    <div style={{ maxWidth: '600px', margin: '1rem 0' }}>
      <h3>SKU vs New MSKU Count</h3>
      <Bar data={chartData} options={{ responsive: true }} />
    </div>
  );
}
