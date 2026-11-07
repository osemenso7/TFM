import useAuth from '../Hooks/UseAuth';

import '../Styles/Dashboard.css';

const Dashboard = () => {

    const { user, token } = useAuth();

    return (
        <div className="dashboard">
            <h1>Dashboard</h1>
            <h1>{ user.username }</h1>
            <h1>{ token }</h1>
        </div>
    );
};

export default Dashboard;