import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAdmin } from '../admin.model';
import { AdminService } from '../service/admin.service';

@Component({
  standalone: true,
  templateUrl: './admin-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AdminDeleteDialogComponent {
  admin?: IAdmin;

  protected adminService = inject(AdminService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.adminService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
