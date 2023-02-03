import React, { useLayoutEffect } from 'react';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { logout } from 'app/shared/reducers/authentication';
import { Alert } from 'reactstrap';
import { Link } from 'react-router-dom';

export const Logout = () => {
  const logoutUrl = useAppSelector(state => state.authentication.logoutUrl);
  const dispatch = useAppDispatch();

  useLayoutEffect(() => {
    dispatch(logout());
    if (logoutUrl) {
      window.location.href = logoutUrl;
    }
  });

  return (
    <div className="p-5">
      <h4>Logged out successfully!</h4>
      <Alert color="warning">
        <Link to="/login" className="alert-link">
          sign in
        </Link>
      </Alert>
    </div>
  );
};

export default Logout;
