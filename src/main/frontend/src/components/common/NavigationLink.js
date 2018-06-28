import styled from 'styled-components';
import { prop, ifProp } from 'styled-tools';

export const NavigationLink = styled.span`
  color: ${prop('color', 'white')};
  padding: 10px;
  background: ${prop('background', '#FA4C50')};
  border: 1px solid #3c3c46;
  display: ${prop('display', 'inline-block')};
  &:before {
    content: '';
    background-image: url('${prop('icon', 'none')}');
    width: ${ifProp('icon', '20px', 'auto')};
    height: ${ifProp('icon', '20px', 'auto')};
    float: left;
    background-size: cover;
    margin-right: ${ifProp('icon', '10px', '0')};
  }
`;
