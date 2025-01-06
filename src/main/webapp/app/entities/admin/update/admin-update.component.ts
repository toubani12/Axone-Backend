import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IWallet } from 'app/entities/wallet/wallet.model';
import { WalletService } from 'app/entities/wallet/service/wallet.service';
import { AdminService } from '../service/admin.service';
import { IAdmin } from '../admin.model';
import { AdminFormService, AdminFormGroup } from './admin-form.service';

@Component({
  standalone: true,
  selector: 'jhi-admin-update',
  templateUrl: './admin-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AdminUpdateComponent implements OnInit {
  isSaving = false;
  admin: IAdmin | null = null;

  usersSharedCollection: IUser[] = [];
  systemWalletsCollection: IWallet[] = [];

  protected adminService = inject(AdminService);
  protected adminFormService = inject(AdminFormService);
  protected userService = inject(UserService);
  protected walletService = inject(WalletService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AdminFormGroup = this.adminFormService.createAdminFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareWallet = (o1: IWallet | null, o2: IWallet | null): boolean => this.walletService.compareWallet(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ admin }) => {
      this.admin = admin;
      if (admin) {
        this.updateForm(admin);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const admin = this.adminFormService.getAdmin(this.editForm);
    if (admin.id !== null) {
      this.subscribeToSaveResponse(this.adminService.update(admin));
    } else {
      this.subscribeToSaveResponse(this.adminService.create(admin));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdmin>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(admin: IAdmin): void {
    this.admin = admin;
    this.adminFormService.resetForm(this.editForm, admin);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, admin.relatedUser);
    this.systemWalletsCollection = this.walletService.addWalletToCollectionIfMissing<IWallet>(
      this.systemWalletsCollection,
      admin.systemWallet,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.admin?.relatedUser)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.walletService
      .query({ filter: 'admin-is-null' })
      .pipe(map((res: HttpResponse<IWallet[]>) => res.body ?? []))
      .pipe(map((wallets: IWallet[]) => this.walletService.addWalletToCollectionIfMissing<IWallet>(wallets, this.admin?.systemWallet)))
      .subscribe((wallets: IWallet[]) => (this.systemWalletsCollection = wallets));
  }
}
