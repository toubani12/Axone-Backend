import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAppAccountType } from '../app-account-type.model';
import { AppAccountTypeService } from '../service/app-account-type.service';

@Component({
  standalone: true,
  templateUrl: './app-account-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AppAccountTypeDeleteDialogComponent {
  appAccountType?: IAppAccountType;

  protected appAccountTypeService = inject(AppAccountTypeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.appAccountTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
