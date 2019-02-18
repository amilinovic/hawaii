import { Formik } from 'formik';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import { createEmployee } from '../store/actions/employeeActions';
import { requestTeams } from '../store/actions/teamsActions';
import { getTeams } from '../store/selectors';
class CreateEmployee extends Component {
  state = {
    employee: {
      leaveProfileId: 1,
      userStatusType: 'ACTIVE'
    }
  };

  componentDidMount() {
    this.props.requestTeams();
  }

  render() {
    if (!this.props.teams) return null;
    const teams = this.props.teams.map(team => {
      return (
        <option key={team.id} value={team.id}>
          {team.name}
        </option>
      );
    });

    return (
      <div className="d-flex p-4 justify-content-center flex-column">
        <Formik
          initialValues={{
            leaveProfileId: 2,
            userRole: 'HR_MANAGER',
            userStatusType: 'ACTIVE'
          }}
          onSubmit={this.props.createEmployee}
        >
          {({ handleSubmit, handleChange }) => (
            <React.Fragment>
              <input
                className="mb-3"
                name="fullName"
                type="text"
                onChange={handleChange}
                placeholder="Full Name"
              />
              <input
                className="mb-3"
                name="email"
                type="text"
                onChange={handleChange}
                placeholder="email"
              />
              <input
                className="mb-3"
                name="jobTitle"
                type="text"
                onChange={handleChange}
                placeholder="Job title"
              />
              <select className="mb-3" name="userRole" onChange={handleChange}>
                <option defaultValue value="HR_MANAGER">
                  HR manager
                </option>
              </select>
              <select className="mb-3" name="teamId" onChange={handleChange}>
                {teams}
              </select>
              <input
                className="mb-3"
                name="startedWorkingDate"
                type="text"
                onChange={handleChange}
                placeholder="Started working date"
              />
              <input
                className="mb-3"
                name="startedWorkingAtExecomDate"
                type="text"
                onChange={handleChange}
                placeholder="Started working at execom date"
              />
              <input
                className="mb-3"
                name="yearsOfService"
                type="text"
                onChange={handleChange}
                placeholder="Years of service"
              />
              <button className="btn" onClick={handleSubmit} type="submit">
                Create
              </button>
            </React.Fragment>
          )}
        </Formik>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  teams: getTeams(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ createEmployee, requestTeams }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(CreateEmployee));
