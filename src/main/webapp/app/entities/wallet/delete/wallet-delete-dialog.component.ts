import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IWallet } from '../wallet.model';
import { WalletService } from '../service/wallet.service';

@Component({
  standalone: true,
  templateUrl: './wallet-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WalletDeleteDialogComponent {
  wallet?: IWallet;

  protected walletService = inject(WalletService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.walletService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
