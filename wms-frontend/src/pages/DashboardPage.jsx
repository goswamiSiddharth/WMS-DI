// import React from 'react';
import MskuChart from '../components/MskuChart';
import LogsTable from '../components/LogsTable';

export default function DashboardPage() {
  return (
    <div>
      <h2>Dashboard View</h2>
      <MskuChart />
      <LogsTable />
    </div>
  );
}
