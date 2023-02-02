import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';

import { Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <div>
      {account?.login ? (
        <div>
          <Alert color="success">You are logged in as user &quot;{account.login}&quot;.</Alert>
        </div>
      ) : (
        <div>
          <Alert color="warning">
            <Link to="/login" className="alert-link">
              sign in
            </Link>
          </Alert>

          <Alert color="warning">
            You don&apos;t have an account yet?&nbsp;
            <Link to="/account/register" className="alert-link">
              Register a new account
            </Link>
          </Alert>
        </div>
      )}
    </div>
  );
};

export default Home;
