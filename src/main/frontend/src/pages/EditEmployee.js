import React, { Component } from 'react';
import { Field, Form } from 'react-final-form';
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
        <Form
          initialValues={this.props.employee}
          onSubmit={this.props.updateEmployee}
        >
          {({ handleSubmit }) => (
            <React.Fragment>
              <Field className="mb-3" name="fullName" component="input" />
              <Field className="mb-3" name="email" component="input" />
              <Field className="mb-3" name="jobTitle" component="input" />
              <Field className="mb-3" name="userRole" component="select">
                <option defaultValue value="HR_MANAGER">
                  HR manager
                </option>
              </Field>
              <Field className="mb-3" name="teamId" component="select">
                {teams}
              </Field>
              <Field
                className="mb-3"
                name="startedWorkingDate"
                component="input"
              />
              <Field
                className="mb-3"
                name="startedWorkingAtExecomDate"
                component="input"
              />
              <Field className="mb-3" name="yearsOfService" component="input" />
              <button className="btn" onClick={handleSubmit} type="submit">
                Update
              </button>
            </React.Fragment>
          )}
        </Form>
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
