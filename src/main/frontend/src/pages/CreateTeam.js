import arrayMutators from 'final-form-arrays';
import React, { Component } from 'react';
import { Field, Form } from 'react-final-form';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import { requestEmployees } from '../store/actions/employeesAction';
import { createTeam } from '../store/actions/teamActions';
import { getEmployees } from '../store/selectors';
class CreateTeam extends Component {
  componentDidMount() {
    this.props.requestEmployees();
  }

  render() {
    if (!this.props.employees) return null;
    return (
      <div className="d-flex p-4 justify-content-center flex-column">
        <Form
          initialValues={{
            teamApprovers: [],
            users: []
          }}
          onSubmit={this.props.createTeam}
          mutators={{
            ...arrayMutators
          }}
          render={({
            handleSubmit,
            values,
            form: {
              mutators: { push }
            }
          }) => (
            <React.Fragment>
              <Field className="mb-3" name="name" component="input" />
              {this.props.employees.map(employee => {
                return (
                  <label
                    className="d-flex justify-content-between"
                    key={employee.id}
                  >
                    {employee.fullName}
                    <div className="mb-2">
                      <button
                        className="btn mr-2"
                        type="button"
                        onClick={() => push('users', employee)}
                      >
                        Add member
                      </button>
                      <button
                        className="btn"
                        type="button"
                        onClick={() => push('teamApprovers', employee)}
                      >
                        Add approver
                      </button>
                    </div>
                  </label>
                );
              })}
              <div className="d-flex justify-content-between mt-3">
                <div className="mb-5">
                  <h3>Team members</h3>
                  {values.users &&
                    values.users.map(user => {
                      return <h5 key={user.id}>{user.fullName}</h5>;
                    })}
                </div>
                <div className="mb-5">
                  <h3>Team approvers</h3>
                  {values.teamApprovers &&
                    values.teamApprovers.map(user => {
                      return <h5 key={user.id}>{user.fullName}</h5>;
                    })}
                </div>
              </div>
              <button className="btn" onClick={handleSubmit} type="submit">
                Update
              </button>
            </React.Fragment>
          )}
        />
      </div>
    );
  }
}

const mapStateToProps = state => ({
  employees: getEmployees(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ createTeam, requestEmployees }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(CreateTeam));
