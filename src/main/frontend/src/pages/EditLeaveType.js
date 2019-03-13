import { Formik } from 'formik';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import Switch from 'react-switch';
import { bindActionCreators } from 'redux';
import * as Yup from 'yup';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import Loading from '../components/loading/Loading';
import {
  requestLeaveType,
  updateLeaveType
} from '../store/actions/leaveTypeActions';
import { getLeaveType } from '../store/selectors';

const validationSchema = Yup.object().shape({
  name: Yup.string().required()
});

class EditLeaveType extends Component {
  componentDidMount() {
    this.props.requestLeaveType(this.props.match.params.id);
  }

  render() {
    if (!this.props.leaveType) return <Loading />;

    return (
      <div className="d-flex p-4 justify-content-center flex-column">
        <Formik
          validationSchema={validationSchema}
          initialValues={this.props.leaveType}
          onSubmit={this.props.updateLeaveType}
          enableReinitialize
          render={({
            handleSubmit,
            handleChange,
            values,
            errors,
            touched,
            setFieldValue
          }) => (
            <React.Fragment>
              <input
                className={`${
                  errors.name && touched.name ? 'border-danger' : ''
                } w-100 mb-3 border`}
                name="name"
                defaultValue={values.name}
                placeholder="Team name"
                onChange={handleChange}
              />
              <input
                className="w-100 border mb-3"
                name="comment"
                defaultValue={values.comment}
                placeholder="Sickness request emails"
                onChange={handleChange}
              />
              <div className="d-flex justify-content-between mb-3">
                <h5>Active</h5>
                <Switch
                  onChange={() => setFieldValue('active', !values.active)}
                  checked={values.active}
                  name="active"
                />
              </div>
              <button className="btn" onClick={handleSubmit} type="submit">
                Update leave type
              </button>
            </React.Fragment>
          )}
        />
      </div>
    );
  }
}

const mapStateToProps = state => ({
  leaveType: getLeaveType(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ requestLeaveType, updateLeaveType }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(EditLeaveType));
