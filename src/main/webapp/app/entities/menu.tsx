import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';
import { NavLink } from 'reactstrap';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="money-check" to="/bank-account">
        My Accounts
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
