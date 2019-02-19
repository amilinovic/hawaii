import { Formik } from 'formik';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import {
  requestEmployee,
  updateEmployee
} from '../store/actions/employeeActions';
import { requestTeams } from '../store/actions/teamsActions';
import { getEmployee, getTeams } from '../store/selectors';

class EditEmployee extends Component {
  componentDidMount() {
    this.props.requestEmployee(this.props.match.params.id);
    this.props.requestTeams();
  }

  render() {
    if (!this.props.employee) return null;

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
          initialValues={this.props.employee}
          onSubmit={this.props.updateEmployee}
          enableReinitialize
        >
          {({ handleSubmit, handleChange, values }) => (
            <React.Fragment>
              <input
                className="mb-3"
                name="fullName"
                onChange={handleChange}
                defaultValue={values.fullName}
                placeholder="Employee name"
              />
              <input
                className="mb-3"
                name="email"
                onChange={handleChange}
                defaultValue={values.email}
                placeholder="Employee email"
              />
              <input
                className="mb-3"
                name="jobTitle"
                onChange={handleChange}
                defaultValue={values.jobTitle}
                placeholder="Employee job title"
              />
              <select className="mb-3" name="userRole">
                <option value="HR_MANAGER">HR manager</option>
              </select>
              <select className="mb-3" name="teamId">
                {teams}
              </select>
              <input
                className="mb-3"
                name="startedWorkingDate"
                onChange={handleChange}
                defaultValue={values.startedWorkingDate}
                placeholder="Started working date"
              />
              <input
                className="mb-3"
                name="startedWorkingAtExecomDate"
                onChange={handleChange}
                defaultValue={values.startedWorkingAtExecomDate}
                placeholder="Started working at execom date"
              />
              <input
                className="mb-3"
                name="yearsOfService"
                onChange={handleChange}
                defaultValue={values.yearsOfService}
                placeholder="Years of service"
              />
              <button className="btn" onClick={handleSubmit} type="submit">
                Update
              </button>
            </React.Fragment>
          )}
        </Formik>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  employee: getEmployee(state),
  teams: getTeams(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators(
    { updateEmployee, requestEmployee, requestTeams },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(EditEmployee));
