import { FieldArray, Formik } from 'formik';
import React, { Component } from 'react';
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
        <Formik
          initialValues={{
            teamApprovers: [],
            users: []
          }}
          onSubmit={this.props.createTeam}
          render={({ handleSubmit, handleChange, values }) => (
            <React.Fragment>
              <input className="mb-3" name="name" onChange={handleChange} />
              {this.props.employees.map(employee => {
                return (
                  <label
                    className="d-flex justify-content-between"
                    key={employee.id}
                  >
                    {employee.fullName}
                    <div className="mb-2">
                      <FieldArray
                        name="users"
                        render={arrayHelpers => (
                          <button
                            className="btn mr-2"
                            type="button"
                            onClick={() => arrayHelpers.push(employee)}
                          >
                            Add member
                          </button>
                        )}
                      />
                      <FieldArray
                        name="teamApprovers"
                        render={arrayHelpers => (
                          <button
                            className="btn"
                            type="button"
                            onClick={() => arrayHelpers.push(employee)}
                          >
                            Add approver
                          </button>
                        )}
                      />
                    </div>
                  </label>
                );
              })}
              <div className="d-flex justify-content-between mt-3">
                <div className="mb-5">
                  <h3>Team members</h3>
                  {values.users.map(user => {
                    return <h5 key={user.id}>{user.fullName}</h5>;
                  })}
                </div>
                <div className="mb-5">
                  <h3>Team approvers</h3>
                  {values.teamApprovers.map(user => {
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
