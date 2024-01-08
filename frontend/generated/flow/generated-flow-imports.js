import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/vertical-layout/theme/lumo/vaadin-vertical-layout.js';
import '@vaadin/login/theme/lumo/vaadin-login-overlay.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/vaadin-lumo-styles/color-global.js';
import '@vaadin/vaadin-lumo-styles/typography-global.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === '5254e076e4368ecfddf54f946b50151ead75f4e50949671dbdae190b9641fab6') {
    pending.push(import('./chunks/chunk-7329b0b62eaf2c4fa5d3ff8059e550e2cb965b89910e3f514747e631f2ecb319.js'));
  }
  if (key === '6f68047775f77bde8280646939942d77412ba57302d7c8408d2e18c95ff5fdc1') {
    pending.push(import('./chunks/chunk-aadc60133a4f78716aecbb7f05d859a1380e37ac0e5f169783dda3dbf6db25bd.js'));
  }
  if (key === '80dbbfd5048bb2ca77e43f97c7c8f127e77c4547a4df224b81609f29765876c6') {
    pending.push(import('./chunks/chunk-aadc60133a4f78716aecbb7f05d859a1380e37ac0e5f169783dda3dbf6db25bd.js'));
  }
  if (key === '70c2c70a55051915a6c27082b66117498632dbf7deb62418879f5bea5a70d360') {
    pending.push(import('./chunks/chunk-2cbd3c3f7c3d9ee84e1aabe2960cfd8c75adec6c191239f511b2e9246da6f913.js'));
  }
  if (key === '0dcb21ccffa754810d60794c6671c1fd0117c573ed08987ea0e5b01a623e6f4b') {
    pending.push(import('./chunks/chunk-4c0569ccc6629b1aa38e1316df74cdd729b9fc0d32b84270eab30e3816b37907.js'));
  }
  if (key === '5b09064b58b60bffc97bbfac00d16ceb20e9fc54d5946f677270f6c0d8c5b9ed') {
    pending.push(import('./chunks/chunk-4c0569ccc6629b1aa38e1316df74cdd729b9fc0d32b84270eab30e3816b37907.js'));
  }
  if (key === '298073bc6bf08cb5a17c4b1efed00b3ecfdaf8c8fd3d044f79174233ee4221d4') {
    pending.push(import('./chunks/chunk-2cbd3c3f7c3d9ee84e1aabe2960cfd8c75adec6c191239f511b2e9246da6f913.js'));
  }
  if (key === '11cc6e2e2dfdb8c5baa466ff1d6f949d20370a3fc8f61c6916a8989436a70a97') {
    pending.push(import('./chunks/chunk-4c0569ccc6629b1aa38e1316df74cdd729b9fc0d32b84270eab30e3816b37907.js'));
  }
  if (key === '7087b3edf59addeac31bdb5f56adf92bc1c99caeebf905beca61e5386c7c5580') {
    pending.push(import('./chunks/chunk-fb6fd68dadae929c98929f93487254597206f38ab568782fbabe69e258a4ee09.js'));
  }
  if (key === 'e11edad1d987a0639ee99be563f948020c15dd9a0dc48f9c838a2fed70c4ab8a') {
    pending.push(import('./chunks/chunk-2cbd3c3f7c3d9ee84e1aabe2960cfd8c75adec6c191239f511b2e9246da6f913.js'));
  }
  if (key === 'd0b4d0b6426bfdcb59f89e75480a1e6355cf25fd43b2e32e94619bff077d5bbb') {
    pending.push(import('./chunks/chunk-aadc60133a4f78716aecbb7f05d859a1380e37ac0e5f169783dda3dbf6db25bd.js'));
  }
  if (key === 'cafd9441429083177067edcf3ea66e4ab6005f0046360fb7977862c2b3602c5f') {
    pending.push(import('./chunks/chunk-8e5dfe8495dc5afe9e0835436268c7637951a4d9341e00c05785d27b62a43c75.js'));
  }
  if (key === 'bb0872fb00188bf555a868f23257b79029c1c2d303ff87322e9fa064d33652a5') {
    pending.push(import('./chunks/chunk-4c0569ccc6629b1aa38e1316df74cdd729b9fc0d32b84270eab30e3816b37907.js'));
  }
  if (key === '4ec73405d947587255e645aac282e36488ffef460d48d6606a0bc4517f8c4b34') {
    pending.push(import('./chunks/chunk-aadc60133a4f78716aecbb7f05d859a1380e37ac0e5f169783dda3dbf6db25bd.js'));
  }
  if (key === '4cbd5d0342de4ff17b8c09e772de4e6b896f8369ea615a2f58ccb12888f6a3da') {
    pending.push(import('./chunks/chunk-58f78cfb7b8fccc802a1b9e36c4d53a8741eb473739fed52cc7e778ea03f1a62.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;
window.Vaadin.Flow.resetFocus = () => {
 let ae=document.activeElement;
 while(ae&&ae.shadowRoot) ae = ae.shadowRoot.activeElement;
 return !ae || ae.blur() || ae.focus() || true;
}