import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IContractType } from '../contract-type.model';
import { ContractTypeService } from '../service/contract-type.service';

@Component({
  standalone: true,
  templateUrl: './contract-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ContractTypeDeleteDialogComponent {
  contractType?: IContractType;

  protected contractTypeService = inject(ContractTypeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contractTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
