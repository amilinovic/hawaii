import { Formik } from 'formik';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as Yup from 'yup';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import {
  requestEmployee,
  updateEmployee
} from '../store/actions/employeeActions';
import { requestTeams } from '../store/actions/teamsActions';
import { getEmployee, getTeams } from '../store/selectors';

const validationSchema = Yup.object().shape({
  fullName: Yup.string().required('Required'),
  email: Yup.string()
    .email('Invalid email')
    .required('Required'),
  userRole: Yup.string().required('Required'),
  teamId: Yup.string().required('Required'),
  jobTitle: Yup.string().required('Required'),
  startedWorkingAtExecomDate: Yup.string().required('Required'),
  startedWorkingDate: Yup.string().required('Required')
});

class EditEmployee extends Component {
  componentDidMount() {
    this.props.requestEmployee(this.props.match.params.id);
    this.props.requestTeams();
  }

  render() {
    if (!this.props.employee || !this.props.teams) return null;

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
          validationSchema={validationSchema}
          initialValues={this.props.employee}
          onSubmit={this.props.updateEmployee}
          enableReinitialize
        >
          {({ handleSubmit, handleChange, values, errors, touched }) => (
            <React.Fragment>
              <input
                className={`${
                  errors.fullName && touched.fullName
                    ? 'border border-danger'
                    : 'border'
                } mb-3`}
                name="fullName"
                onChange={handleChange}
                defaultValue={values.fullName}
                placeholder="Employee name"
              />
              <input
                className={`${
                  errors.email && touched.email
                    ? 'border border-danger'
                    : 'border'
                } mb-3`}
                name="email"
                onChange={handleChange}
                defaultValue={values.email}
                placeholder="Employee email"
              />
              <input
                className={`${
                  errors.jobTitle && touched.jobTitle
                    ? 'border border-danger'
                    : 'border'
                } mb-3`}
                name="jobTitle"
                onChange={handleChange}
                defaultValue={values.jobTitle}
                placeholder="Employee job title"
              />
              <select
                className={`${
                  errors.userRole && touched.userRole
                    ? 'border border-danger'
                    : 'border'
                } mb-3`}
                name="userRole"
                onChange={handleChange}
                value={values.userRole}
              >
                <option value="HR_MANAGER">HR manager</option>
                <option value="DEVELOPER">Developer</option>
              </select>
              <select
                className={`${
                  errors.teamId && touched.teamId
                    ? 'border border-danger'
                    : 'border'
                } mb-3`}
                name="teamId"
                onChange={handleChange}
                value={values.teamId}
              >
                {teams}
              </select>
              <input
                className={`${
                  errors.startedWorkingDate && touched.startedWorkingDate
                    ? 'border border-danger'
                    : 'border'
                } mb-3`}
                name="startedWorkingDate"
                onChange={handleChange}
                defaultValue={values.startedWorkingDate}
                placeholder="Started working date"
              />
              <input
                className={`${
                  errors.startedWorkingAtExecomDate &&
                  touched.startedWorkingAtExecomDate
                    ? 'border border-danger'
                    : 'border'
                } mb-3`}
                name="startedWorkingAtExecomDate"
                onChange={handleChange}
                defaultValue={values.startedWorkingAtExecomDate}
                placeholder="Started working at execom date"
              />
              <input
                className="mb-3 border"
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
