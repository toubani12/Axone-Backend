import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IAppAccount } from 'app/entities/app-account/app-account.model';
import { AppAccountService } from 'app/entities/app-account/service/app-account.service';
import { WalletService } from '../service/wallet.service';
import { IWallet } from '../wallet.model';
import { WalletFormService } from './wallet-form.service';

import { WalletUpdateComponent } from './wallet-update.component';

describe('Wallet Management Update Component', () => {
  let comp: WalletUpdateComponent;
  let fixture: ComponentFixture<WalletUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let walletFormService: WalletFormService;
  let walletService: WalletService;
  let appAccountService: AppAccountService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, WalletUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(WalletUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WalletUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    walletFormService = TestBed.inject(WalletFormService);
    walletService = TestBed.inject(WalletService);
    appAccountService = TestBed.inject(AppAccountService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call relatedToAccount query and add missing value', () => {
      const wallet: IWallet = { id: 456 };
      const relatedToAccount: IAppAccount = { id: 11124 };
      wallet.relatedToAccount = relatedToAccount;

      const relatedToAccountCollection: IAppAccount[] = [{ id: 20914 }];
      jest.spyOn(appAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: relatedToAccountCollection })));
      const expectedCollection: IAppAccount[] = [relatedToAccount, ...relatedToAccountCollection];
      jest.spyOn(appAccountService, 'addAppAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ wallet });
      comp.ngOnInit();

      expect(appAccountService.query).toHaveBeenCalled();
      expect(appAccountService.addAppAccountToCollectionIfMissing).toHaveBeenCalledWith(relatedToAccountCollection, relatedToAccount);
      expect(comp.relatedToAccountsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const wallet: IWallet = { id: 456 };
      const relatedToAccount: IAppAccount = { id: 17138 };
      wallet.relatedToAccount = relatedToAccount;

      activatedRoute.data = of({ wallet });
      comp.ngOnInit();

      expect(comp.relatedToAccountsCollection).toContain(relatedToAccount);
      expect(comp.wallet).toEqual(wallet);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWallet>>();
      const wallet = { id: 123 };
      jest.spyOn(walletFormService, 'getWallet').mockReturnValue(wallet);
      jest.spyOn(walletService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wallet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wallet }));
      saveSubject.complete();

      // THEN
      expect(walletFormService.getWallet).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(walletService.update).toHaveBeenCalledWith(expect.objectContaining(wallet));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWallet>>();
      const wallet = { id: 123 };
      jest.spyOn(walletFormService, 'getWallet').mockReturnValue({ id: null });
      jest.spyOn(walletService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wallet: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wallet }));
      saveSubject.complete();

      // THEN
      expect(walletFormService.getWallet).toHaveBeenCalled();
      expect(walletService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWallet>>();
      const wallet = { id: 123 };
      jest.spyOn(walletService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wallet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(walletService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAppAccount', () => {
      it('Should forward to appAccountService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(appAccountService, 'compareAppAccount');
        comp.compareAppAccount(entity, entity2);
        expect(appAccountService.compareAppAccount).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
