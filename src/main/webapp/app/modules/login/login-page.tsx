import React from 'react';
import { ValidatedField } from 'react-jhipster';
import { Button, Alert, Row, Col, Form } from 'reactstrap';
import { Link } from 'react-router-dom';
import { useForm } from 'react-hook-form';

export interface ILoginProps {
  loginError: boolean;
  handleLogin: (username: string, password: string, rememberMe: boolean) => void;
}

const LoginPage = (props: ILoginProps) => {
  const login = ({ username, password, rememberMe }) => {
    props.handleLogin(username, password, rememberMe);
  };

  const {
    handleSubmit,
    register,
    formState: { errors, touchedFields },
  } = useForm({ mode: 'onTouched' });

  const { loginError } = props;

  const handleLoginSubmit = e => {
    handleSubmit(login)(e);
  };

  return (
    <div>
      <Form onSubmit={handleLoginSubmit}>
        <Row className="justify-content-center">
          <Col md="8">
            <h1 id="login-title" data-cy="loginTitle">
              Sign In
            </h1>
          </Col>
        </Row>
        <Row>
          <Col md="12">
            {loginError ? (
              <Alert color="danger" data-cy="loginError">
                <strong>Failed to sign in!</strong> Please check your credentials and try again.
              </Alert>
            ) : null}
          </Col>
          <Col md="12">
            <ValidatedField
              name="username"
              label="Username"
              placeholder="Your username"
              required
              autoFocus
              data-cy="username"
              validate={{ required: 'Username cannot be empty!' }}
              register={register}
              error={errors.username}
              isTouched={touchedFields.username}
            />
            <ValidatedField
              name="password"
              type="password"
              label="Password"
              placeholder="Your password"
              required
              data-cy="password"
              validate={{ required: 'Password cannot be empty!' }}
              register={register}
              error={errors.password}
              isTouched={touchedFields.password}
            />
            <ValidatedField name="rememberMe" type="checkbox" check label="Remember me" value={true} register={register} />
          </Col>
        </Row>
        <div className="mt-1">&nbsp;</div>
        <Alert color="warning">
          <Link to="/account/reset/request" data-cy="forgetYourPasswordSelector">
            Did you forget your password?
          </Link>
        </Alert>
        <Alert color="warning">
          <span>You don&apos;t have an account yet?</span> <Link to="/account/register">Register a new account</Link>
        </Alert>
        <Row>
          <Button color="primary" type="submit" data-cy="submit">
            Sign in
          </Button>
        </Row>
      </Form>
    </div>
  );
};

export default LoginPage;
