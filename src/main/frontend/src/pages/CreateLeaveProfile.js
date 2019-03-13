import { Formik } from 'formik';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as Yup from 'yup';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import { createLeaveProfile } from '../store/actions/leaveProfileActions';

const validationSchema = Yup.object().shape({
  name: Yup.string().required(),
  entitlement: Yup.string().required(),
  maxCarriedOver: Yup.string().required(),
  maxBonusDays: Yup.string().required(),
  maxAllowanceFromNextYear: Yup.string().required(),
  training: Yup.string().required()
});

class CreateLeaveProfile extends Component {
  render() {
    return (
      <div className="d-flex p-4 justify-content-center flex-column">
        <Formik
          validationSchema={validationSchema}
          initialValues={{
            name: '',
            entitlement: '',
            maxCarriedOver: '',
            maxBonusDays: '',
            maxAllowanceFromNextYear: '',
            training: ''
          }}
          onSubmit={this.props.createLeaveProfile}
          render={({ handleSubmit, handleChange, values, errors, touched }) => (
            <React.Fragment>
              <input
                className={`${
                  errors.name && touched.name ? 'border-danger' : ''
                } w-100 mb-3 border`}
                name="name"
                defaultValue={values.name}
                placeholder="Leave profile name"
                onChange={handleChange}
              />
              <input
                className={`${
                  errors.entitlement && touched.entitlement
                    ? 'border-danger'
                    : ''
                } w-100 mb-3 border`}
                name="entitlement"
                defaultValue={values.entitlement}
                placeholder="Entitlement"
                onChange={handleChange}
              />
              <input
                className={`${
                  errors.maxCarriedOver && touched.maxCarriedOver
                    ? 'border-danger'
                    : ''
                } w-100 mb-3 border`}
                name="maxCarriedOver"
                defaultValue={values.maxCarriedOver}
                placeholder="Max carried over"
                onChange={handleChange}
              />
              <input
                className={`${
                  errors.maxBonusDays && touched.maxBonusDays
                    ? 'border-danger'
                    : ''
                } w-100 mb-3 border`}
                name="maxBonusDays"
                defaultValue={values.maxBonusDays}
                placeholder="Max bonus days"
                onChange={handleChange}
              />
              <input
                className={`${
                  errors.maxAllowanceFromNextYear &&
                  touched.maxAllowanceFromNextYear
                    ? 'border-danger'
                    : ''
                } w-100 mb-3 border`}
                name="maxAllowanceFromNextYear"
                defaultValue={values.maxAllowanceFromNextYear}
                placeholder="Max allowance from next year"
                onChange={handleChange}
              />
              <input
                className={`${
                  errors.training && touched.training ? 'border-danger' : ''
                } w-100 mb-3 border`}
                name="training"
                defaultValue={values.training}
                placeholder="Max training"
                onChange={handleChange}
              />
              <textarea
                className="border mb-3"
                name="comment"
                defaultValue={values.comment}
                onChange={handleChange}
                placeholder="Comment"
              />

              <button className="btn" onClick={handleSubmit} type="submit">
                Create leave profile
              </button>
            </React.Fragment>
          )}
        />
      </div>
    );
  }
}

const mapDispatchToProps = dispatch =>
  bindActionCreators({ createLeaveProfile }, dispatch);

export default connect(
  null,
  mapDispatchToProps
)(withResetOnNavigate()(CreateLeaveProfile));
