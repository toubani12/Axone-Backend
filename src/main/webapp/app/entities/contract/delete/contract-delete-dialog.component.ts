import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IContract } from '../contract.model';
import { ContractService } from '../service/contract.service';

@Component({
  standalone: true,
  templateUrl: './contract-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ContractDeleteDialogComponent {
  contract?: IContract;

  protected contractService = inject(ContractService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contractService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
