package com.tigapermata.sewagudangapps.api;

import com.tigapermata.sewagudangapps.model.GudangList;
import com.tigapermata.sewagudangapps.model.inbound.AddItemResponse;
import com.tigapermata.sewagudangapps.model.inbound.AutocompleteItemList;
import com.tigapermata.sewagudangapps.model.inbound.CekItemUpdate;
import com.tigapermata.sewagudangapps.model.inbound.CheckedListbyItem;
import com.tigapermata.sewagudangapps.model.inbound.DataLabelItemList;
import com.tigapermata.sewagudangapps.model.inbound.FormIncomingList;
import com.tigapermata.sewagudangapps.model.inbound.FormPackingList;
import com.tigapermata.sewagudangapps.model.inbound.InboundDetailList;
import com.tigapermata.sewagudangapps.model.inbound.InboundList;
import com.tigapermata.sewagudangapps.model.inbound.IncomingList;
import com.tigapermata.sewagudangapps.model.LoginResponse;
import com.tigapermata.sewagudangapps.model.ProjectList;
import com.tigapermata.sewagudangapps.model.inbound.ItemIncomingChecked;
import com.tigapermata.sewagudangapps.model.inbound.ItemIncomingDetail;
import com.tigapermata.sewagudangapps.model.inbound.LocatorList;
import com.tigapermata.sewagudangapps.model.inbound.RawFormPackinglist;
import com.tigapermata.sewagudangapps.model.inbound.StatusOK;
import com.tigapermata.sewagudangapps.model.inbound.RawFormIncoming;
import com.tigapermata.sewagudangapps.model.outbound.DataOutgoingList;
import com.tigapermata.sewagudangapps.model.outbound.DataPickingChecklist;
import com.tigapermata.sewagudangapps.model.outbound.DataPickingList;
import com.tigapermata.sewagudangapps.model.outbound.DataPickingListByItem;
import com.tigapermata.sewagudangapps.model.outbound.FormOutgoingList;
import com.tigapermata.sewagudangapps.model.outbound.ItemOutgoingChecked;
import com.tigapermata.sewagudangapps.model.outbound.ItemOutgoingDetail;
import com.tigapermata.sewagudangapps.model.outbound.LoadingChecklistResponse;
import com.tigapermata.sewagudangapps.model.outbound.OutboundList;
import com.tigapermata.sewagudangapps.model.outbound.PickingResponse;
import com.tigapermata.sewagudangapps.model.outbound.StatusPicking;
import com.tigapermata.sewagudangapps.model.putaway.DataFilterByItemList;
import com.tigapermata.sewagudangapps.model.putaway.DataFilterByLabelList;
import com.tigapermata.sewagudangapps.model.putaway.DataHistoryList;
import com.tigapermata.sewagudangapps.model.putaway.DataLocatorList;
import com.tigapermata.sewagudangapps.model.putaway.DataSearched;
import com.tigapermata.sewagudangapps.model.putaway.DataSearchedList;
import com.tigapermata.sewagudangapps.model.putaway.StatusPutAway;
import com.tigapermata.sewagudangapps.model.stockcount.AddDetailStockCountResponse;
import com.tigapermata.sewagudangapps.model.stockcount.AddMasterStockCountResponse;
import com.tigapermata.sewagudangapps.model.stockcount.DetailByItemList;
import com.tigapermata.sewagudangapps.model.stockcount.DetailByLabelList;
import com.tigapermata.sewagudangapps.model.stockcount.StockCountList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    //--------------------LOGIN--------------------//

    //for confirm password from link in email
    @POST("confirm")
    @FormUrlEncoded
    Call<LoginResponse> confirmPassword(@Field("token") String token,
                                        @Field("password") String password);

    //for login
    @POST("loginmobile")
    @FormUrlEncoded
    Call<LoginResponse> login(@Field("email") String email,
                              @Field("password") String password);

    //--------------------SEARCH GUDANG AND PROJECT--------------------//

    //for search gudang that user has access
    @POST("allgudanguser")
    @FormUrlEncoded
    Call<GudangList> searchGudang(@Field("id_user") String user,
                                  @Field("token") String token);

    //for search project that user has access
    @POST("allprojectuser")
    @FormUrlEncoded
    Call<ProjectList> searchProject(@Field("id_user") String user,
                                    @Field("token") String token,
                                    @Field("id_gudang") String gudang);

    //--------------------INBOUND--------------------//

    //show list inbound based on gudang and project that has been choosen
    @POST("{id_gudang}/{id_project}/inbound")
    @FormUrlEncoded
    Call<InboundList> listInbound(@Path("id_gudang") String idGudang,
                                  @Path("id_project") String idProject,
                                  @Field("id_user") String user,
                                  @Field("token") String token);

    //show list incoming based on inbound that has been choosen
    @POST("{id_gudang}/{id_project}/inbound/{id_inbound}")
    @FormUrlEncoded
    Call<IncomingList> listIncoming(@Path("id_gudang") String idGudang,
                                    @Path("id_project") String idProject,
                                    @Path("id_inbound") String idInbound,
                                    @Field("id_user") String user,
                                    @Field("token") String token);

    //show incoming form that user must be input
    @POST("{id_gudang}/{id_project}/incomingForm")
    @FormUrlEncoded
    Call<FormIncomingList> listFormIncoming(@Path("id_gudang") String idGudang,
                                            @Path("id_project") String idProject,
                                            @Field("id_user") String user,
                                            @Field("token") String token);

    //add incoming, this data was sent based on incoming form
    @POST("{id_gudang}/{id_project}/inbound/{id_inbound}/incomingAdd")
    Call<StatusOK> addIncoming(@Path("id_gudang") String idGudang,
                               @Path("id_project") String idProject,
                               @Path("id_inbound") String idInbound,
                               @Body RawFormIncoming incomingAdd);

    //show list item that will be checking
    @POST("{id_gudang}/{id_project}/inbound/{id_inbound}/dataitemcheked/{id_incoming}")
    @FormUrlEncoded
    Call<ItemIncomingChecked> dataItemIncomingChecked(@Path("id_gudang") String idGudang,
                                                      @Path("id_project") String idProject,
                                                      @Path("id_inbound") String idInbound,
                                                      @Path("id_incoming") String idIncoming,
                                                      @Field("id_user") String user,
                                                      @Field("token") String token);

    //send item data that has been checking
    @POST("{id_gudang}/{id_project}/inbound/{id_inbound}/incomingChecklist/{id_incoming}")
    @FormUrlEncoded
    Call<ItemIncomingDetail> incomingChecklist(@Path("id_gudang") String idGudang,
                                               @Path("id_project") String idProject,
                                               @Path("id_inbound") String idInbound,
                                               @Path("id_incoming") String idIncoming,
                                               @Field("id_user") String user,
                                               @Field("token") String token,
                                               @Field("label") String label);

    //show list of locator for autocomplete locator
    @POST("{id_gudang}/{id_project}/inbound/{id_inbound}/autocompleteLocator")
    @FormUrlEncoded
    Call<LocatorList> requestLocator(@Path("id_gudang") String idGudang,
                                     @Path("id_project") String idProject,
                                     @Path("id_inbound") String idInbound,
                                     @Field("id_user") String user,
                                     @Field("token") String token);

    //for add item that has been checking
    @POST("{id_gudang}/{id_project}/inbound/{id_inbound}/incomingChecklistInput/{id_incoming}")
    @FormUrlEncoded
    Call<StatusOK> incomingChecklistInput(@Path("id_gudang") String idGudang,
                                          @Path("id_project") String idProject,
                                          @Path("id_inbound") String idInbound,
                                          @Path("id_incoming") String idIncoming,
                                          @Field("id_user") String user,
                                          @Field("token") String token,
                                          @Field("qty") String qty,
                                          @Field("id_inbound_detail") String idInboundDetail,
                                          @Field("nama_locator") String namaLocator);

    //show item detail in inbound
    @POST("{id_gudang}/{id_project}/inboundDetail/{id_inbound}")
    @FormUrlEncoded
    Call<InboundDetailList> listDetailInbound(@Path("id_gudang") String idGudang,
                                              @Path("id_project") String idProject,
                                              @Path("id_inbound") String idInbound,
                                              @Field("id_user") String user,
                                              @Field("token") String token);

    //show list for autocomplete item
    @POST("{id_gudang}/{id_project}/inbound/{id_inbound}/autocompleteItem")
    @FormUrlEncoded
    Call<AutocompleteItemList> autoCompleteItemIncoming(@Path("id_gudang") String idGudang,
                                                        @Path("id_project") String idProject,
                                                        @Path("id_inbound") String idInbound,
                                                        @Field("id_user") String user,
                                                        @Field("token") String token);

    //for checking form packing list
    @POST("{id_gudang}/{id_project}/inbound/{id_inbound}/formPackingList")
    @FormUrlEncoded
    Call<FormPackingList> packinglistCheck(@Path("id_gudang") String idGudang,
                                           @Path("id_project") String idProject,
                                           @Path("id_inbound") String idInbound,
                                           @Field("id_user") String user,
                                           @Field("token") String token);

    //add packing list, this data was sent based on packing list form
    @POST("{id_gudang}/{id_project}/inbound/{id_inbound}/addItemPackingList")
    Call<StatusOK> addPackinglist(@Path("id_gudang") String idGudang,
                                  @Path("id_project") String idProject,
                                  @Path("id_inbound") String idInbound,
                                  @Body RawFormPackinglist packingAdd);

    //add master item, this data is send image too
    @Multipart
    @POST("{id_gudang}/{id_project}/addItem/{id_inbound}")
    Call<AddItemResponse> addMasterItem(@Path("id_gudang") String idGudang,
                                        @Path("id_project") String idProject,
                                        @Path("id_inbound") String idInbound,
                                        @Part("id_user") RequestBody user,
                                        @Part("token") RequestBody token,
                                        @Part("jenis_generated") RequestBody jenisGenerated,
                                        @Part("kode_item") RequestBody kodeItem,
                                        @Part MultipartBody.Part foto,
                                        @Part("hitung_cbm") RequestBody hitungCBM,
                                        @Part("panjang") RequestBody panjang,
                                        @Part("lebar") RequestBody lebar,
                                        @Part("tinggi") RequestBody tinggi,
                                        @Part("total_cbm") RequestBody totalCBM,
                                        @Part("nama_item") RequestBody namaItem,
                                        @Part("uom") RequestBody uom,
                                        @Part("tonase") RequestBody tonase,
                                        @Part("satuan_tonase") RequestBody satuanTonase,
                                        @Part("berat_kotor") RequestBody beratKotor);

    //add master item without image upload
    @Multipart
    @POST("{id_gudang}/{id_project}/addItem/{id_inbound}")
    Call<AddItemResponse> addMasterItemWithoutPhoto(@Path("id_gudang") String idGudang,
                                                    @Path("id_project") String idProject,
                                                    @Path("id_inbound") String idInbound,
                                                    @Part("id_user") RequestBody user,
                                                    @Part("token") RequestBody token,
                                                    @Part("jenis_generated") RequestBody jenisGenerated,
                                                    @Part("kode_item") RequestBody kodeItem,
                                                    @Part("hitung_cbm") RequestBody hitungCBM,
                                                    @Part("panjang") RequestBody panjang,
                                                    @Part("lebar") RequestBody lebar,
                                                    @Part("tinggi") RequestBody tinggi,
                                                    @Part("total_cbm") RequestBody totalCBM,
                                                    @Part("nama_item") RequestBody namaItem,
                                                    @Part("uom") RequestBody uom,
                                                    @Part("tonase") RequestBody tonase,
                                                    @Part("satuan_tonase") RequestBody satuanTonase,
                                                    @Part("berat_kotor") RequestBody beratKotor);

    //checked list by label
    @POST("{id_gudang}/{id_project}/inbound/{id_inbound}/datalabelcheked/{id_incoming}")
    @FormUrlEncoded
    Call<CheckedListbyItem> itemChecklistByItem(@Path("id_gudang") String idGudang,
                                                @Path("id_project") String idProject,
                                                @Path("id_inbound") String idInbound,
                                                @Path("id_incoming") String idIncoming,
                                                @Field("id_user") String user,
                                                @Field("token") String token);

    //send data on incoming by item
    @POST("{id_gudang}/{id_project}/inbound/{id_inbound}/scanLabelItem/{id_incoming}")
    @FormUrlEncoded
    Call<StatusOK> sendDataLabel(@Path("id_gudang") String idGudang,
                                 @Path("id_project") String idProject,
                                 @Path("id_inbound") String idInbound,
                                 @Path("id_incoming") String idIncoming,
                                 @Field("id_user") String user,
                                 @Field("token") String token,
                                 @Field("id_item") String idItem,
                                 @Field("label") String label);

    //show item that has been scan
    @POST("{id_gudang}/{id_project}/inbound/{id_inbound}/dataLabelItem/{id_incoming}")
    @FormUrlEncoded
    Call<DataLabelItemList> dataLabelCheck(@Path("id_gudang") String idGudang,
                                           @Path("id_project") String idProject,
                                           @Path("id_inbound") String idInbound,
                                           @Path("id_incoming") String idIncoming,
                                           @Field("id_user") String user,
                                           @Field("token") String token,
                                           @Field("id_item") String idItem);

    //check qty update
    @POST("{id_gudang}/{id_project}/inbound/{id_inbound}/cekItemUptodate")
    @FormUrlEncoded
    Call<CekItemUpdate> cekItem(@Path("id_gudang") String idGudang,
                                @Path("id_project") String idProject,
                                @Path("id_inbound") String idInbound,
                                @Field("id_user") String user,
                                @Field("token") String token,
                                @Field("id_item") String idItem);

    //delete label
    @POST("{id_gudang}/{id_project}/inbound/{id_inbound}/hapusLabel")
    @FormUrlEncoded
    Call<StatusPutAway> deleteLabel(@Path("id_gudang") String idGudang,
                                    @Path("id_project") String idProject,
                                    @Path("id_inbound") String idInbound,
                                    @Field("id_user") String user,
                                    @Field("token") String token,
                                    @Field("id_item") String idItem,
                                    @Field("label") String label);

    //--------------------PUTAWAY--------------------//

    //show list of locator data
    @POST("{id_gudang}/{id_project}/DataLocator")
    @FormUrlEncoded
    Call<DataLocatorList> showLocator(@Path("id_gudang") String idGudang,
                                      @Path("id_project") String idProject,
                                      @Field("id_user") String user,
                                      @Field("token") String token);

    //show list of filter by item
    @POST("{id_gudang}/{id_project}/filterItem2/item")
    @FormUrlEncoded
    Call<DataFilterByItemList> showFilterByItem(@Path("id_gudang") String idGudang,
                                                @Path("id_project") String idProject,
                                                @Field("id_user") String user,
                                                @Field("token") String token);

    //show list of filter by label
    @POST("{id_gudang}/{id_project}/filterItem2/label")
    @FormUrlEncoded
    Call<DataFilterByLabelList> showFilterByLabel(@Path("id_gudang") String idGudang,
                                                  @Path("id_project") String idProject,
                                                  @Field("id_user") String user,
                                                  @Field("token") String token);

    //show selected item by item
    @POST("{id_gudang}/{id_project}/cariDataItem/item")
    @FormUrlEncoded
    Call<DataSearchedList> cariDataItemByItem(@Path("id_gudang") String idGudang,
                                              @Path("id_project") String idProject,
                                              @Field("id_user") String user,
                                              @Field("token") String token,
                                              @Field("filter_item") String filter);

    //show selected item by item
    @POST("{id_gudang}/{id_project}/cariDataItem/label")
    @FormUrlEncoded
    Call<DataSearchedList> cariDataItemByLabel(@Path("id_gudang") String idGudang,
                                               @Path("id_project") String idProject,
                                               @Field("id_user") String user,
                                               @Field("token") String token,
                                               @Field("filter_item") String filter);

    //move item
    @POST("{id_gudang}/{id_project}/simpanLocatorMove")
    @FormUrlEncoded
    Call<StatusPutAway> locatorMove(@Path("id_gudang") String idGudang,
                                    @Path("id_project") String idProject,
                                    @Field("id_user") String user,
                                    @Field("token") String token,
                                    @Field("id_locator_baru") String idLocatorBaru,
                                    @Field("id_locator_lama") String idLocatorLama,
                                    @Field("id_inventory_detail") String idInventoryDetail,
                                    @Field("qty") String qty);

    //history putaway
    @POST("{id_gudang}/{id_project}/putawayHistory")
    @FormUrlEncoded
    Call<DataHistoryList> putawayHistory(@Path("id_gudang") String idGudang,
                                         @Path("id_project") String idProject,
                                         @Field("id_user") String user,
                                         @Field("token") String token);

    //send data item to server
    @POST("{id_gudang}/{id_project}/movelocator")
    @FormUrlEncoded
    Call<StatusPutAway> putAwayFunc(@Path("id_gudang") String idGudang,
                                    @Path("id_project") String idProject,
                                    @Field("id_user") String user,
                                    @Field("token") String token,
                                    @Field("id_locator_lama") String idLocatorLama,
                                    @Field("id_locator_baru") String idLocatorBaru,
                                    @Field("qty") String qty,
                                    @Field("id_item") String idItem,
                                    @Field("id_inventory_detail") String idInventoryDetail);

    //checking lebel on put away
    @POST("{id_gudang}/{id_project}/ceklabelscan")
    @FormUrlEncoded
    Call<StatusPutAway> checkLabelPutAway(@Path("id_gudang") String idGudang,
                                          @Path("id_project") String idProject,
                                          @Field("id_user") String user,
                                          @Field("token") String token,
                                          @Field("id_locator") String idLocator,
                                          @Field("label") String label);


    //--------------------OUTBOUND--------------------//

    //show outbound list based on picking status
    @POST("{id_gudang}/{id_project}/outbound/listPicking")
    @FormUrlEncoded
    Call<OutboundList> showbyPicking(@Path("id_gudang") String idGudang,
                                     @Path("id_project") String idProject,
                                     @Field("id_user") String user,
                                     @Field("token") String token);

    //show outbound list based on picking status
    @POST("{id_gudang}/{id_project}/outbound/listLoading")
    @FormUrlEncoded
    Call<OutboundList> showbyLoading(@Path("id_gudang") String idGudang,
                                     @Path("id_project") String idProject,
                                     @Field("id_user") String user,
                                     @Field("token") String token);

    //show list picking
    @POST("{id_gudang}/{id_project}/outbound/getDataPicking/{id_outbound}")
    @FormUrlEncoded
    Call<DataPickingList> showPickingList(@Path("id_gudang") String idGudang,
                                          @Path("id_project") String idProject,
                                          @Path("id_outbound") String idOutbound,
                                          @Field("id_user") String user,
                                          @Field("token") String token);

    //show list picking by item
    @POST("{id_gudang}/{id_project}/outbound/getDataPicking/{id_outbound}")
    @FormUrlEncoded
    Call<DataPickingListByItem> showPickingListByItem(@Path("id_gudang") String idGudang,
                                                      @Path("id_project") String idProject,
                                                      @Path("id_outbound") String idOutbound,
                                                      @Field("id_user") String user,
                                                      @Field("token") String token);

    //show list label that is picked
    @POST("{id_gudang}/{id_project}/outbound/getDataPickingByitem/{id_outbound}")
    @FormUrlEncoded
    Call<DataPickingChecklist> showListLabelPicked(@Path("id_gudang") String idGudang,
                                                   @Path("id_project") String idProject,
                                                   @Path("id_outbound") String idOutbound,
                                                   @Field("id_user") String user,
                                                   @Field("token") String token,
                                                   @Field("id_item") String idItem);

    //cancel picking
    @POST("{id_gudang}/{id_project}/outbound/cancelPicking/{id_outbound}")
    @FormUrlEncoded
    Call<StatusPutAway> cancelPicking(@Path("id_gudang") String idGudang,
                                            @Path("id_project") String idProject,
                                            @Path("id_outbound") String idOutbound,
                                            @Field("id_user") String user,
                                            @Field("token") String token,
                                            @Field("id_outbound_detail") String idOutboundDetail);

    //scan label picking by item
    @POST("{id_gudang}/{id_project}/outbound/scanItemLabel/{id_outbound}")
    @FormUrlEncoded
    Call<StatusPicking> scanItemLabelPicking(@Path("id_gudang") String idGudang,
                                             @Path("id_project") String idProject,
                                             @Path("id_outbound") String idOutbound,
                                             @Field("id_user") String user,
                                             @Field("token") String token,
                                             @Field("label") String label,
                                             @Field("id_item") String idItem);

    //picking process by clicked button
    @POST("{id_gudang}/{id_project}/outbound/prosesPicking/{id_outbound}")
    @FormUrlEncoded
    Call<PickingResponse> pickingProcessbyClick(@Path("id_gudang") String idGudang,
                                                @Path("id_project") String idProject,
                                                @Path("id_outbound") String idOutbound,
                                                @Field("id_user") String user,
                                                @Field("token") String token,
                                                @Field("id_inventory_detail") String idInventory,
                                                @Field("id_outbound_detail") String idOutboundDetail);

    //undo picking process
    @POST("{id_gudang}/{id_project}/outbound/undo/{id_outbound}")
    @FormUrlEncoded
    Call<StatusPutAway> undoPickingProcess(@Path("id_gudang") String idGudang,
                                           @Path("id_project") String idProject,
                                           @Path("id_outbound") String idOutbound,
                                           @Field("id_user") String user,
                                           @Field("token") String token,
                                           @Field("id_inventory_detail") String idInventory,
                                           @Field("id_outbound_detail") String idOutboundDetail);

    //picking scan
    @POST("{id_gudang}/{id_project}/outbound/prosesPickingScan/{id_outbound}")
    @FormUrlEncoded
    Call<StatusPutAway> pickingScan(@Path("id_gudang") String idGudang,
                                    @Path("id_project") String idProject,
                                    @Path("id_outbound") String idOutbound,
                                    @Field("id_user") String user,
                                    @Field("token") String token,
                                    @Field("label") String label);

    //change status picking to picked
    @POST("{id_gudang}/{id_project}/outbound/picked/{id_outbound}")
    @FormUrlEncoded
    Call<StatusPutAway> pickingToPicked(@Path("id_gudang") String idGudang,
                                    @Path("id_project") String idProject,
                                    @Path("id_outbound") String idOutbound,
                                    @Field("id_user") String user,
                                    @Field("token") String token);

    //show list outgoing
    @POST("{id_gudang}/{id_project}/outbound/outgoing/{id_outbound}")
    @FormUrlEncoded
    Call<DataOutgoingList> showOutgoing(@Path("id_gudang") String idGudang,
                                        @Path("id_project") String idProject,
                                        @Path("id_outbound") String idOutbound,
                                        @Field("id_user") String user,
                                        @Field("token") String token);

    //show outgoing form that user must be input
    @POST("{id_gudang}/{id_project}/outbound/outgoingForm")
    @FormUrlEncoded
    Call<FormOutgoingList> listFormOutgoing(@Path("id_gudang") String idGudang,
                                            @Path("id_project") String idProject,
                                            @Field("id_user") String user,
                                            @Field("token") String token);

    //add outgoing, this data was sent based on outgoing form
    @POST("{id_gudang}/{id_project}/outbound/outgoing/{id_outbound}/addoutgoing")
    Call<StatusOK> addOutgoing(@Path("id_gudang") String idGudang,
                               @Path("id_project") String idProject,
                               @Path("id_outbound") String idOutbound,
                               @Body RawFormIncoming outgoingAdd);

    //show list loading item
    @POST("{id_gudang}/{id_project}/outbound/{id_outbound}/listItemPicked/{id_outgoing}")
    @FormUrlEncoded
    Call<ItemOutgoingChecked> dataItemOutgoingChecked(@Path("id_gudang") String idGudang,
                                                      @Path("id_project") String idProject,
                                                      @Path("id_outbound") String idInbound,
                                                      @Path("id_outgoing") String idIncoming,
                                                      @Field("id_user") String user,
                                                      @Field("token") String token);

    //send data to check loading item
    @POST("{id_gudang}/{id_project}/outbound/{id_outbound}/prosesLoading/{id_outgoing}")
    @FormUrlEncoded
    Call<ItemOutgoingDetail> outgoingChecklist(@Path("id_gudang") String idGudang,
                                               @Path("id_project") String idProject,
                                               @Path("id_outbound") String idOutbound,
                                               @Path("id_outgoing") String idOutgoing,
                                               @Field("id_user") String user,
                                               @Field("token") String token,
                                               @Field("id_outbound_detail") String idOutboundDetail,
                                               @Field("id_inventory_detail") String idInventoryDetail,
                                               @Field("qty") String qty);

    //send data to check loading item by search label
    @POST("{id_gudang}/{id_project}/outbound/{id_outbound}/outgoingChecklist/{id_outgoing}")
    @FormUrlEncoded
    Call<LoadingChecklistResponse> outgoingChecklistByLabel(@Path("id_gudang") String idGudang,
                                                            @Path("id_project") String idProject,
                                                            @Path("id_outbound") String idOutbound,
                                                            @Path("id_outgoing") String idOutgoing,
                                                            @Field("id_user") String user,
                                                            @Field("token") String token,
                                                            @Field("label") String label);


    //--------------------STOCK COUNT--------------------//

    //show stock count list
    @POST("{id_gudang}/{id_project}/stockCount/listStockCount")
    @FormUrlEncoded
    Call<StockCountList> showListStockCount(@Path("id_gudang") String idGudang,
                                            @Path("id_project") String idProject,
                                            @Field("id_user") String user,
                                            @Field("token") String token);

    //add master stock count
    @POST("{id_gudang}/{id_project}/stockCount/addStockCount")
    @FormUrlEncoded
    Call<AddMasterStockCountResponse> addMasterStockCount(@Path("id_gudang") String idGudang,
                                                          @Path("id_project") String idProject,
                                                          @Field("id_user") String user,
                                                          @Field("token") String token,
                                                          @Field("tanggal") String tanggal);

    //show detail stock count by item
    @POST("{id_gudang}/{id_project}/stockCount/detailStockCount/{id_stockcount}/by/item")
    @FormUrlEncoded
    Call<DetailByItemList> showDetailStockCountByItem(@Path("id_gudang") String idGudang,
                                                      @Path("id_project") String idProject,
                                                      @Path("id_stockcount") String idStockcount,
                                                      @Field("id_user") String user,
                                                      @Field("token") String token);

    //show detail stock count by label
    @POST("{id_gudang}/{id_project}/stockCount/detailStockCount/{id_stockcount}/by/label")
    @FormUrlEncoded
    Call<DetailByLabelList> showDetailStockCountByLabel(@Path("id_gudang") String idGudang,
                                                        @Path("id_project") String idProject,
                                                        @Path("id_stockcount") String idStockcount,
                                                        @Field("id_user") String user,
                                                        @Field("token") String token);

    //add detail stock count by item
    @POST("{id_gudang}/{id_project}/stockCount/addDetailStockCount/{id_stockcount}/by/item")
    @FormUrlEncoded
    Call<AddDetailStockCountResponse> addDetailStockCountByItem(@Path("id_gudang") String idGudang,
                                                                @Path("id_project") String idProject,
                                                                @Path("id_stockcount") String idStockcount,
                                                                @Field("id_user") String user,
                                                                @Field("token") String token,
                                                                @Field("id_locator") String idLocator,
                                                                @Field("item") String kodeItem,
                                                                @Field("qty") String qty);

    //add detail stock count by label
    @POST("{id_gudang}/{id_project}/stockCount/addDetailStockCount/{id_stockcount}/by/label")
    @FormUrlEncoded
    Call<AddDetailStockCountResponse> addDetailStockCountByLabel(@Path("id_gudang") String idGudang,
                                                                @Path("id_project") String idProject,
                                                                @Path("id_stockcount") String idStockcount,
                                                                @Field("id_user") String user,
                                                                @Field("token") String token,
                                                                @Field("id_locator") String idLocator,
                                                                @Field("item") String label,
                                                                @Field("qty") String qty);
}
